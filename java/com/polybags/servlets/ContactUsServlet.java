/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import com.google.gson.Gson;
import com.polybags.beans.AuditHandler;
import com.polybags.beans.Email;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.activation.DataHandler;

/**
 *
 * @author bencleary
 */
@WebServlet(name = "ContactUsServlet", urlPatterns = {"/ContactUs"})
public class ContactUsServlet extends HttpServlet {

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

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/contactUsView.jsp");
        dispatcher.forward(request, response);
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
        String firstname = request.getParameter("first_name");
        String lastname = request.getParameter("last_name");
        String emailaddress = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        //Boolean hasError = false;
        //String error = "";
        Boolean sent = false;

        try {

            if (emailaddress != null || firstname != null || lastname != null || subject != null || message != null) {
                Email email = new Email(emailaddress, firstname, lastname, subject, message, null);
                sent = MyUtils.sendEmailTojustbagit(email);
                if (sent) {
                    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/productListView.jsp");
                    dispatcher.forward(request, response);
                } else {
                    System.out.println("Failed at some point to send message");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // error = e.getMessage();
            //hasError = true;
        }
        /*finally {
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
        }*/
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
