/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import com.google.gson.Gson;
import com.polybags.beans.AuditHandler;
//import com.polybags.beans.Cart;
//import com.polybags.beans.Discount;
import com.polybags.beans.LeadTime;
import com.polybags.beans.Product;
import com.polybags.beans.PromoCodes;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author bencleary
 */
@WebServlet(name = "returnCartServlet", urlPatterns = {"/returnCart"})
public class returnCartServlet extends HttpServlet {

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
        Connection conn = MyUtils.getStoredConnection(request);
        BigDecimal total = new BigDecimal(0);
        BigDecimal subtotal = new BigDecimal(0);
        Map<String, BigDecimal> totals = new HashMap<>();
        String JSONArray = "";
        String error = "";
        Boolean hasError = false, flag = false;
        String errorId = "", promocodeJSON = "";
        Map<String, String> errorJSON = new HashMap<>();

        try {
            int leadtime = DBUtils.getMaxLeadTime(conn);

            Cookie cartCookie = MyUtils.getCookie(request, "cart");
            List<Product> cart = DBUtils.getCart(conn, cartCookie.getValue());
            PromoCodes promocode = DBUtils.getIntermediaryPromocode(conn, cartCookie.getValue());

            if (!cart.isEmpty()) {
                totals = MyUtils.calculateProductTotals(conn, cart, subtotal, total);
                leadtime = MyUtils.calculateOrderLeadTime(cart, leadtime);
                List<LeadTime> leadtimeObj = DBUtils.getLeadTimes(conn, leadtime);

                if (promocode != null) {
                    total = promocode.applyPromocode(totals.get("total"));
                    totals.put("total", total);
                    promocodeJSON = new Gson().toJson(promocode);
                    flag = true;
                } else {
                }

                String subtotalJSON = new Gson().toJson(totals.get("subtotal"));
                String leadtimeJSON = new Gson().toJson(leadtimeObj);
                String totalJSON = new Gson().toJson(totals.get("total"));
                String cartJSON = new Gson().toJson(cart);
                if (flag) {
                    JSONArray = "[" + totalJSON + "," + subtotalJSON + "," + leadtimeJSON + "," + promocodeJSON + "," + cartJSON + "]";
                }
                if (!flag) {
                    JSONArray = "[" + totalJSON + "," + subtotalJSON + "," + leadtimeJSON + ",\"empty\"," + cartJSON + "]";
                }
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSONArray);
            } else {
                AuditHandler audit = new AuditHandler(null, "returnCartServletGET", "products can not be found - potential tamper", request.getSession().getId());
                errorId = DBUtils.logAudit(conn, audit);
                errorJSON.put("redirect", "Y");
                errorJSON.put("id", errorId);
                String errorString = new Gson().toJson(errorJSON);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorString);

            }
        } catch (Exception e) {
            error = e.getMessage();
            hasError = true;
        } finally {
            if (hasError) {
                AuditHandler audit = new AuditHandler(error, "returnCartServletGET", "Exception caught", request.getSession().getId());
                try {
                    errorId = DBUtils.logAudit(conn, audit);
                    request.setAttribute(errorId, "id");
                } catch (Exception e) {
                }
                errorJSON.put("redirect", "Y");
                errorJSON.put("id", errorId);
                String errorString = new Gson().toJson(errorJSON);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorString);
            }
        }

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
        String promocode = request.getParameter("promocode");
        promocode = promocode.replace("\"", "");
        String error = "";
        String errorId = "";
        String JSONArray = "";
        String promocodeJSON = "";
        Boolean hasError = false, flag = false;
        BigDecimal total = new BigDecimal(0);
        BigDecimal subtotal = new BigDecimal(0);
        Map<String, String> errorJSON = new HashMap<>();

        try {
            Cookie cart = MyUtils.getCookie(request, "cart");
            List<Product> products = DBUtils.getCart(conn, cart.getValue());
            if (!products.isEmpty()) {
                Map<String, BigDecimal> totals = MyUtils.calculateProductTotals(conn, products, subtotal, total);
                PromoCodes promoCode = DBUtils.getPromoCode(conn, promocode);
                if (promoCode != null) {
                    flag = true;
                    total = promoCode.applyPromocode(totals.get("total"));
                    totals.put("total", total);
                    DBUtils.intermediaryPromocode(conn, promoCode.getDescription(), cart.getValue());
                }

                String subtotalJSON = new Gson().toJson(totals.get("subtotal"));
                String totalJSON = new Gson().toJson(totals.get("total"));

                
                if (flag) {
                    promocodeJSON = new Gson().toJson(promoCode);                   
                }
                if (!flag) {
                    promocodeJSON = new Gson().toJson("empty");
                }
                JSONArray = "[" + totalJSON + "," + subtotalJSON + "," + promocodeJSON + "]";
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSONArray);
            } else {
                AuditHandler audit = new AuditHandler(null, "returnCartServletPOST", "products (get cart) can not be found - potential tamper", request.getSession().getId());
                errorId = "" + DBUtils.logAudit(conn, audit);
                errorJSON.put("redirect", "Y");
                errorJSON.put("id", errorId);
                String errorString = new Gson().toJson(errorJSON);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorString);
            }
        } catch (Exception e) {
            error = e.getMessage();
            hasError = true;
        } finally {
            if (hasError) {
                AuditHandler audit = new AuditHandler(error, "addToCartServlet", "Exception caught", request.getSession().getId());
                try {
                    errorId = "" + DBUtils.logAudit(conn, audit);
                } catch (Exception e) {
                }
                errorJSON.put("redirect", "Y");
                errorJSON.put("id", errorId);
                String errorString = new Gson().toJson(errorJSON);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorString);
            }
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
