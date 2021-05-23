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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bencleary
 */
@WebServlet(name = "ProductDataServlet", urlPatterns = {"/productData"})
public class ProductDataServlet extends HttpServlet {

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
        String error = "";
        Boolean hasError = false;
        String productId = request.getParameter("id");
        System.out.println("productId parameter: " + productId);
        if (productId != null) {
            int id = Integer.parseInt(request.getParameter("id"));

            try {

                Product product = DBUtils.getProduct(conn, id);
                if(product != null){
                    System.out.println(product.getSize());
                    String productJSON = new Gson().toJson(product);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(productJSON);
                }else{
                    hasError = true;
                    error = "can not find product for product page";
                }

            } catch (Exception e) {
                error = e.getMessage();
                hasError = true;
            } finally {
                if (hasError) {
                    AuditHandler audit = new AuditHandler(error, "productDataServletGET", "Exception caught", request.getSession().getId());
                    try {
                        String errorId = "" + DBUtils.logAudit(conn, audit);
                        request.setAttribute(errorId, "id");
                    } catch (Exception e) {
                    }
                    request.setAttribute(error, "error");
                    response.sendRedirect(request.getServletPath() + "/error");

                }
            }
        } else {
            AuditHandler audit = new AuditHandler(null, "addToCartServlet", "product can not be found - potential tamper", request.getSession().getId());
            try {
                String id = "" + DBUtils.logAudit(conn, audit);
                request.setAttribute(id, "id");
            } catch (Exception e) {
            } finally {
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
