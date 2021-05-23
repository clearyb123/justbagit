/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.polybags.beans.AuditHandler;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author bencleary
 */
@WebServlet(name = "newsletterServlet", urlPatterns = {"/newsletter"})
public class newsletterServlet extends HttpServlet {

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
        String error = "";
        Boolean hasError = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = in.readLine();
        while (line != null) {
            body += line;
            line = in.readLine();
        }

        try {
            JSONObject jsonObject = new JSONObject(body);
            String emailaddress = jsonObject.getString("emailaddress");

            DBUtils.insertNewsletterEmailAddress(conn, emailaddress);
        } catch (Exception e) {
            error = e.getMessage();
            hasError = true;
        } finally {
            in.close();
            if (hasError) {
                AuditHandler audit = new AuditHandler(error, "newsletterServlet", "Exception caught", request.getSession().getId());
                try {
                    String errorId = "" + DBUtils.logAudit(conn, audit);
                    request.setAttribute(errorId, "id");
                } catch (Exception e) {
                }
                request.setAttribute(error, "error");
                response.sendRedirect(request.getServletPath() + "/error");

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
