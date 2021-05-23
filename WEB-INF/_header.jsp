<%-- 
    Document   : header
    Created on : 28-Dec-2020, 12:00:43
    Author     : bencleary
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang ="en">

    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-173958097-2">
    </script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag() {
            dataLayer.push(arguments);
        }
        gtag('js', new Date());

        gtag('config', 'UA-173958097-2');
    </script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta name="robots" content="index, follow, max-snippet:-1, max-image-preview:large, max-video-preview:-1">
        <meta name="description" content="Therapy and Counselling in Hale, Altrincham and Wilmslow.">
        <meta name="author" content="Ben Cleary">
        <meta name="MobileOptimized" content="320">
        <meta name="HandheldFriendly" content="True">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="keywords" content="Polybags, bags, plastic bags, clear plastic bags, polythene bags, poly bags, poly bags with suffocation warning, self seal bags,  poly,
              polybag, clear poly bags, fba, poly, self seal bag, sealable plastic bags">
        <meta name="robots" content="index, follow" />
        <link type="text/css" rel="stylesheet" href="CSS/stylesheet.css">
        <link rel="stylesheet" href="https://s3.amazonaws.com/codecademy-content/courses/ltp/css/bootstrap.css">
        <link type="text/css" rel="stylesheet" href="CSS/font-awesome-4.7.0/css/font-awesome.min.css">
        <link type="text/css" rel="icon" type="image/gif/png" href="Images/icon.png">
        <meta http-equiv="Content-Type" content="text/html;" charset="UTF-8">

        <title>justbagit</title>
    </head>
    <body>
        <div class="nav-menu">
            <div class="navTitles">
                <div style="display:inline-block" class="Title">
                    <a id="title"href="${pageContext.request.contextPath}/"><img src='Images/logo.png'></a>
                </div>
                <div style="display:inline-block;"class="freeNextWorkingDayDeliveryBanner">
                    <h4>Free Next Working Day Delivery <br>for all orders over £60</h4>
                </div>
            </div>
            <ul class="nav">
                <li class="nav-item" id="productsNav"><a href="${pageContext.request.contextPath}/collection"><strong>PRODUCTS</strong></a></li>
                <li class="nav-item" id="contactNav"><a href="${pageContext.request.contextPath}/ContactUs"><strong>CONTACT US</strong></a></li>
                <li class="nav-item" id="aboutNav"><a  href="${pageContext.request.contextPath}/AboutUs"><strong>ABOUT US</strong></a></li>
                <li class="nav-item" id="myBasketNav"><i class="fa fa-cart-plus fa-3x"></i><div class="cart-items" id="cartItems"></div></li>
            </ul>
        </div>
    </div>

