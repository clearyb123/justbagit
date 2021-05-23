/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import com.google.gson.Gson;
import com.mysql.cj.log.Log;
import com.polybags.beans.AuditHandler;
import com.polybags.beans.LeadTime;
import com.polybags.beans.Order;
import com.polybags.beans.Product;
import com.polybags.beans.PromoCodes;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.CreatePaymentResponse;
import com.squareup.square.models.Money;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author bencleary
 */
@WebServlet(name = "processPaymentServlet", urlPatterns = {"/process-payment"})
public class processPaymentServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = MyUtils.getStoredConnection(request);

        String body = "";
        String JSON = "";
        Map<String, Object> model = new HashMap<>();
        BigDecimal deliveryPrice = new BigDecimal(0);
        Order order = null;
        String sandboxApplicationToken = "EAAAEK72cVGJlL4aprC5hKMb971tqmGmoBPh3am8c9phbKMqCW7Dr35cWt6SPw_4";
        String productionApplicationToken = "EAAAEBYzRA-6pdjF3r2s-jddgz0L6MayKQ12iNf02ECQeBpEYSBfciVfTiNd_Wg5";
        String productionEnviroment = "production";
        String sandboxEnvironment = "sandbox";
        
        BufferedReader in = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = in.readLine();
        while (line != null) {
            body += line;
            line = in.readLine();
        }
        try {
            JSONObject jsonObject = new JSONObject(body);

            String nonce = jsonObject.getString("nonce");
            String idempotency_key = jsonObject.getString("idempotency_key");
            String orderNo = jsonObject.getString("orderNo");
            String basketId = jsonObject.getString("basketId");
            String total = jsonObject.getString("amount");
            JSONObject delivery = new JSONObject(jsonObject.getString("delivery"));

            int deliveryleadtime = Integer.parseInt(delivery.getString("delivery_leadtime"));
            Boolean userSelect = delivery.getBoolean("user_select");

            if (userSelect) {
                LeadTime leadtime = DBUtils.getLeadTime(conn, deliveryleadtime);
                deliveryPrice = leadtime.getPrice();
            }

            try {
                Cookie cart = MyUtils.getCookie(request, "cart");
                List<Product> products = DBUtils.getCartForPaymentCheck(conn, basketId);
                PromoCodes promocode = DBUtils.getIntermediaryPromocode(conn, cart.getValue());
                order = DBUtils.queryOrder(conn, basketId);

                if (promocode != null) {
                    order.setPromocode(promocode);
                }
                order.setProducts(products);
                order.setSubtotal();
                order.setTotal(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            BigDecimal totalCheck = new BigDecimal(total);
            BigDecimal serverTotal = order.getTotal();
            serverTotal = serverTotal.add(deliveryPrice);

            if (totalCheck.compareTo(serverTotal) == 0) {
                total = total.replace(".", "");

                Long amount = Long.parseLong(total);
                SquareClient client = new SquareClient.Builder().environment(Environment.fromString(productionEnviroment)).accessToken(productionApplicationToken).build();
                try {
                    Map<String, Object> status = MyUtils.charge(conn, nonce, amount, "GBP", idempotency_key, orderNo, client);
                    JSON = new Gson().toJson(status);
                    if ((int) status.get("response") != 200) {
                        JSONObject responseError = new JSONObject(JSON);
                        String category = responseError.getJSONObject("error").getString("category");
                        if(category != null && "PAYMENT_METHOD_ERROR".equals(category)){
                            DBUtils.resetOrder(conn, basketId, orderNo);
                        }
                        response.setStatus((int) status.get("response"));
                        AuditHandler audit = new AuditHandler(JSON, getServletContext().getContextPath() + "/process-payment", "F", request.getSession().getId());
                        DBUtils.logAudit(conn, audit);
                    } else {
                        MyUtils.sendOrderEmailToCustomer(order);
                        MyUtils.sendOrderEmailToCompany(order);
                        AuditHandler audit = new AuditHandler(JSON, getServletContext().getContextPath() + "/process-payment", "X", request.getSession().getId());
                        DBUtils.logAudit(conn, audit);
                    }
                } 
                catch (ApiException | IOException | SQLException e) {
                    e.printStackTrace();
                }
                catch (MessagingException ex) {
                    Logger.getLogger(processPaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                response.setStatus(500);
                String error = "Totals do not match. Passed Amount: " + totalCheck + " Actual Order Amount: " + order.getTotal() + " Ensure they are the same to proceed.";
                AuditHandler audit = new AuditHandler(error, getServletContext().getContextPath() + "/process-payment", "F", request.getSession().getId());
                DBUtils.logAudit(conn, audit);
                JSON = new Gson().toJson(model);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON);

        } catch (IOException | NumberFormatException | SQLException | JSONException err) {
            err.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
