/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import com.google.gson.Gson;
import com.polybags.beans.AuditHandler;
import com.polybags.beans.LeadTime;
import com.polybags.beans.Order;
import com.polybags.beans.Product;
import com.polybags.beans.PromoCodes;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author bencleary
 */
@WebServlet(name = "placeOrderServlet", urlPatterns = {"/placeOrder"})
public class placeOrderServlet extends HttpServlet {

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
        //String customerJSON = request.getParameter("customer");
        String customerJSON = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = in.readLine();
        while (line != null) {
            customerJSON += line;
            line = in.readLine();
        }

        HttpSession session = request.getSession();
        String error = "";
        String errorId = "";
        Boolean hasError = false, emailSent = true;
        Map<String, String> errorJSON = new HashMap<>();
        Map<String, Object> responseBasket = new HashMap<>();

        Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(customerJSON);
            String address = jsonObject.getString("customer");

            Order order = gson.fromJson(address, Order.class);
            try {
                Cookie cart = MyUtils.getCookie(request, "cart");
                List<Product> products = DBUtils.getCart(conn, cart.getValue());
                PromoCodes promocode = DBUtils.getIntermediaryPromocode(conn, cart.getValue());
                if (products.isEmpty()) {
                    AuditHandler audit = new AuditHandler(null, "placeOrderServletPOST", "cart can not be found - potential tamper", request.getSession().getId());
                    errorId = "" + DBUtils.logAudit(conn, audit);
                    responseBasket.put("errorId", errorId);
                    responseBasket.put("moveon", false);
                    responseBasket.put("response", 500);

                } else {
                    String basketId = MyUtils.generateBasketId(conn);
                    order.setProducts(products);
                    order.setPromocode(promocode);
                    order.setSubtotal();
                    order.setTotal(conn);
                    order.setCustomerName();
                    order.setBasketId(basketId);
                    order.setPOId(DBUtils.generatePurchaseOrderIdNumber(conn));
                    order.setDueDate(order.getOrderSent().plusDays(order.getLeadTime()).toLocalDate());
                    String orderJSON = new Gson().toJson(order);
                    order.setJSON(orderJSON);

                    DBUtils.placeOrder(conn, order);
                    conn.commit();
                    Boolean orderLoaded = DBUtils.checkOrderLoaded(conn, order.getPOId());
                    //Set email confirmation when live....
                    DBUtils.confirmItemsGPO(conn, basketId, cart.getValue());
                    
                    if (orderLoaded) {
                        responseBasket.put("POId", order.getPOId());
                        responseBasket.put("basketId",basketId);
                        responseBasket.put("moveon", true);
                        responseBasket.put("response", 200);

                    } else {
                        if (!emailSent) {
                            AuditHandler audit = new AuditHandler(error, "placeOrderServlet", "Confirmation email not sent", request.getSession().getId());
                            errorId = DBUtils.logAudit(conn, audit);
                        }
                        if (!orderLoaded) {
                            AuditHandler audit = new AuditHandler(error, "placeOrderServlet", "Problem placing order", request.getSession().getId());
                            errorId = DBUtils.logAudit(conn, audit);
                        }
                        responseBasket.put("errorId", errorId);
                        responseBasket.put("moveon", false);
                        responseBasket.put("response", 500);
                    }

                }

            } catch (Exception e) {
                error = e.getMessage();
                hasError = true;
            } finally {
                in.close();
                if (hasError) {
//                    if (!responseBasket.isEmpty()) {
//
//                        String responseBasketJSON = new Gson().toJson(responseBasket);
//                        System.out.println(responseBasketJSON);
//                        response.setContentType("application/json");
//                        response.setCharacterEncoding("UTF-8");
//                        response.getWriter().write(responseBasketJSON);
//                    } else {
                    AuditHandler audit = new AuditHandler(error, "placeOrderServlet", "Exception caught", request.getSession().getId());
                    try {
                        errorId = DBUtils.logAudit(conn, audit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    responseBasket.put("errorId", errorId);
                    responseBasket.put("moveon", false);
                    responseBasket.put("response", 500);
                }
                String responseBasketJSON = new Gson().toJson(responseBasket);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(responseBasketJSON);

            }
        }
    }




