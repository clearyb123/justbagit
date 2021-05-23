<%-- 
    Document   : AboutUsView
    Created on : 25-Apr-2021, 13:39:37
    Author     : bencleary
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="_header.jsp"></jsp:include>


    <div class="aboutus">
        <div class="container">
            <h1>About Us</h1>
            <div class="aboutusText">
                <p>We are a small family owned business, based in Cheshire, specialising in a great range of strong polythene bags ideal 
                    for storing or shipping a whole variety of goods.<br><br>
                    We pride ourselves on exceptional customer service, great prices and next working day delivery<br><br>
                    We offer a no quibble guarantee and we are happy to discuss regular volume orders.
                    Let us know how we can <a  href="${pageContext.request.contextPath}/ContactUs">help</a>
            </p>
        </div>
    </div>
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
        <button class="checkout-cart banner-btn"><b>CHECKOUT</b></button>
        <p style="color: black;font: 10px;">Items are reserved for 24 hours</p>
    </div>
</div>
<jsp:include page="_footer.jsp"></jsp:include>  
<script src="JS/storeAndBasket.js"></script>
</body>
</html>
