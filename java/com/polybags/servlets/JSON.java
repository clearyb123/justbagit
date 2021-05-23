/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import com.google.gson.Gson;
import com.polybags.beans.AuditHandler;
import com.polybags.beans.Discount;
import com.polybags.beans.Product;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import java.io.IOException;
import java.sql.Connection;
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

/**
 *
 * @author bencleary
 */
@WebServlet(name = "JSON", urlPatterns = {"/JSON"})
public class JSON extends HttpServlet {

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
        Cookie[] cookies = request.getCookies();
        boolean foundCookie = false;
        String cookie = "";
        String error = "", errorId = "";
        Boolean hasError = false;
        Map<String, String> errorJSON = new HashMap<>();
        if(cookies != null){
            for (Cookie cookie1 : cookies) {
                if (cookie1.getName().equals("cart")) {
                    cookie = cookie1.getValue();
                    foundCookie = true;
                }
            }
        }else{
            hasError = true;
            error = "cookies not found!";
        }

        if (!foundCookie) {
            Cookie cookie1 = new Cookie("cart", request.getSession().getId());
            cookie = cookie1.getValue();
            cookie1.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie1);
        }

        try {
            List<Product> productsList = DBUtils.getProducts(conn, cookie);
            HashMap<String, Discount> discountsList = DBUtils.getDiscounts(conn);
            List<Product> cart = DBUtils.getCartCheck(conn, cookie);
            String products = new Gson().toJson(productsList);
            String discounts = new Gson().toJson(discountsList);
            String cartJSON = new Gson().toJson(cart);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String JSON = "[" + products + "," + discounts + "," + cartJSON + "]";
            //String JSON = "[" + products +"]";
            response.getWriter().write(JSON);
        } catch (Exception e) {
            error = e.getMessage();
            hasError = true;
        } finally {
            if (hasError) {
                AuditHandler audit = new AuditHandler(error, "addToCartServlet", "Exception caught", request.getSession().getId());
                try {
                    errorId = "" + DBUtils.logAudit(conn, audit);
                    errorJSON.put("redirect", "Y");
                    errorJSON.put("id", errorId);
                    String errorString = new Gson().toJson(errorJSON);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(errorString);
                } catch (Exception e) {
                }
                request.setAttribute(error, "error");
                response.sendRedirect(request.getServletPath() + "/error");
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
        doGet(request, response);
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
