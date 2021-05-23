<%-- 
    Document   : productListView
    Created on : 28-Dec-2020, 10:48:34
    Author     : bencleary
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.json.*" %>
<jsp:include page="_header.jsp"></jsp:include>
<p style="color: red;">${errorString}</p>

<div class="productsPageBanner">
    <div class="productPageTitle">
        <h1><b>Strong Polythene Bags</b></h1>
    </div>
    <div class="number_of_products">
        <p><i><span id="number"></span> products</i></p>
    </div>
    <div class="productPageDescription">
        <p> All our bags are strong (made from 35 micron LDPE), recyclable and permanent self seal. <br>
            They are ideal for storing a wide variety of goods and produce and are printed with suffocation warnings in 10 languages.<br>
            All bags have an additional 50mm lip to turn over and seal the contents, and are perfect for Amazon FBA</p>
    </div>
</div>

<div class="products">
    <div class="containerS">

    </div>
</div>
<div id="transparentBasket">
</div>
<div id="Basket">
    <div class="continueShopping">
        <p class="inlineblock">Continue Shopping</p> <i class="fa fa-chevron-right inlineblock"></i>
    </div>
    <div  id="basketTitle"> 
        <h2>My Basket</h2>
    </div>
    <div class="cartContent" id="cartContent">   
    </div>
    <div class="cartFooter">
        <div class="subTotal">
            <h5>SubTotal: £</h5>
            <span class="subTotalAmount" id="subTotalAmount"></span>
        </div>
        <div class="Total">
            <h4>Your Total: £</h4>
            <span class="totalAmount" id="totalAmount"></span>
            <span class="discountApplied" id="discountApplied"></span>

        </div>
        <button class="clear-cart banner-btn">CLEAR CART</button>
        <button class="checkout-cart banner-btn notVisible">CHECKOUT</button>
        <p style="color: black;font: 10px;">Items are reserved for 24 hours</p>
    </div>
</div>


<jsp:include page="_footer.jsp"></jsp:include>
<script src="JS/storeAndBasket.js"></script>
</body>
</html>