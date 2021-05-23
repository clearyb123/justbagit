<%-- 
    Document   : _checkoutHeader
    Created on : 19-Mar-2021, 23:13:46
    Author     : bencleary
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang ="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta name="robots" content="index, follow, max-snippet:-1, max-image-preview:large, max-video-preview:-1">
        <meta name="description" content="justbagit | Strong Polythene bags in the UK">
        <meta name="author" content="Ben Cleary">
        <meta name="MobileOptimized" content="320">
        <meta name="HandheldFriendly" content="True">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="keywords" content="Polybags, bags, ecommerce, plastic bags, shipping, UK, packaging bags,packaging, polythene bags, polythene, suffocation warning">
        <meta name="robots" content="index, follow" />
        <link type="text/css" rel="stylesheet" href="CSS/stylesheet.css">
        <link rel="stylesheet" href="https://s3.amazonaws.com/codecademy-content/courses/ltp/css/bootstrap.css">
        <link rel="stylesheet" href="CSS/font-awesome-4.7.0/css/font-awesome.min.css">
        <link rel="icon" type="image/gif/png" href="Images/icon.png">
        <title>justbagit</title>
    </head>
    <body>
        <div class="nav-menu">
            <div class="headerTitle">
                <a id="title"href="${pageContext.request.contextPath}/"><img src='Images/logo.png'></a>
            </div>
            <ul class="nav">
                <li class="nav-item" id="productsNav"><a href="${pageContext.request.contextPath}/collection"><strong>PRODUCTS</strong></a></li>
                <li class="nav-item" id="contactNav"><a href="${pageContext.request.contextPath}/ContactUs"><strong>CONTACT US</strong></a></li>
                <li class="nav-item" id="aboutNav"><a  href="${pageContext.request.contextPath}/AboutUs"><strong>ABOUT US</strong></a></li>
            </ul>
        </div>

