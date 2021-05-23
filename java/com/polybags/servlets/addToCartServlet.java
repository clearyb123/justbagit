/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import com.google.gson.Gson;
import com.polybags.beans.AuditHandler;
import com.polybags.beans.Product;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
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
@WebServlet(name = "addToCartServlet", urlPatterns = {"/addToCart"})
public class addToCartServlet extends HttpServlet {

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
        String data = request.getParameter("product");
        String errorMessage = "";
        String errorId = "";
        Boolean hasError = false;
        Map<String, String> errorJSON = new HashMap<>();

        try {
            if (MyUtils.getCookie(request, "cart") == null) {
                Cookie cookie1 = new Cookie("cart", request.getSession().getId());
                cookie1.setMaxAge(24 * 60 * 60);
                response.addCookie(cookie1);
            }
            /*JSONObject productJSON = new JSONObject(data);
            String productString = productJSON.toString();*/
            Gson gson = new Gson();
            Product product = gson.fromJson(data, Product.class);
            Product productCheck = DBUtils.getProduct(conn, product.getId());

            if (productCheck != null) {
                product.setJSON(data);
                DBUtils.addToCart(conn, product);
            } else {
                AuditHandler audit = new AuditHandler(null, "addToCartServlet", "product can not be found - potential tamper", request.getSession().getId());
                errorId = "" + DBUtils.logAudit(conn, audit);
                errorJSON.put("redirect", "Y");
                errorJSON.put("id", errorId);
                String errorString = new Gson().toJson(errorJSON);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorString);

            }

        } catch (Exception e) {
            errorMessage = e.getMessage();
            if(errorMessage == null){
                errorMessage = "possible null pointer exception eror message is null";
            }
            e.printStackTrace();
            hasError = true;
        } finally {
            if (hasError) {
                AuditHandler audit = new AuditHandler(errorMessage, "addToCartServlet", "Exception caught", request.getSession().getId());
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
