/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.polybags.beans.Product;
import com.polybags.utils.DBUtils;
import com.polybags.utils.MyUtils;
import javax.servlet.http.Cookie;
 
@WebServlet(urlPatterns = { "/collection" })
public class ProductListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public ProductListServlet() {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean foundCookie = false;

        if (!foundCookie) {
            Cookie cookie1 = new Cookie("cart", request.getSession().getId());
            cookie1.setMaxAge(24*60*60);
            response.addCookie(cookie1); 
        }
        // Forward to /WEB-INF/views/productListView.jsp
        RequestDispatcher dispatcher = request.getServletContext()
                .getRequestDispatcher("/WEB-INF/productListView.jsp");
        dispatcher.forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
