<%-- 
    Document   : Home
    Created on : 04-Mar-2021, 20:39:32
    Author     : bencleary
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="_header.jsp"></jsp:include>  
    <div class="hero_large">
        <div class="landingImage">
            <div class="hero_large_inner">
                <h1>Clear Permanent Self Seal Polybags</h1>
                <div type="button" class="btn-primary btn shopNowBtn">
                    <a class="shopNow" href="${pageContext.request.contextPath}/collection">SHOP NOW</a>
            </div>
        </div>
    </div>
</div>
<div class="promoBanner">
    <div class="promoBannerContainer">
        <div class="promoSplashMessages">
            <div  class="promoBannerDescription">
                <span class="inlineblock promoSplash">
                    <p><strong><i style="margin-right: 10px" class="fa fa-trophy"></i>For Special Offers use Launch5 at checkout for 5% off</strong></p>
                </span>
                <span class="inlineblock promoSplash">
                    <p><strong>Discounts available for quantity purchases</strong></p>
                </span>
                <span class="inlineblock promoSplash">
                    <p><strong><i style="margin-right: 10px" class="fa fa-truck"></i>Free Next Working Day Delivery<br>
                            for all orders over £60</strong></p> 
                </span>
            </div>
        </div>
    </div>
</div>
<div class="hero_small">
    <div class="hero_small_inner ">
        <h3>LATEST ARRIVALS</h3>
        <div class="latestproducts">

        </div>                
    </div>
</div>

<div class="hero_large">
    <div class="secondImage">
        <div class="hero_large_inner">
            <h1>Strong 100% Recyclable 35 Micron LDPE bags <br> Made in the UK</h1>
            <div type="button" class="btn-primary btn shopNowBtn">
                <a class="shopNow" href="${pageContext.request.contextPath}/collection">SHOP NOW</a>
            </div>
        </div>
    </div>
</div>
<div class="hero_small">
    <div class="hero_small_inner">
        <div class="quote">
            <h5>"Prompt and efficient service. Item as described. Very pleased. Delivery company exceeded expectations and date provided. Item arrived day earlier than expected outcome. Brilliant, thanks." <br><br><span class="author"><i>- ISSA</i></span></h5>
        </div>
        <div class="quote">
            <h5>"Good quality bags, with a good seal and include suffocation warnings. I chose a mixed pack and they are a nice range of sizes, will be buying more." <br><br> <span class="author"><i>- Mr A Peach</i></span></h5>
        </div>
        <div class="quote">
            <h5>"I use these to display my t-shirts in. Just the perfect size for a child's t-shirt folded neatly." <br><br> <span class="author"><i>- Danny</i></span></h5>
        </div>        
    </div>
</div>
<div class="hero_large">
    <div class="thirdImage">
        <div class="hero_large_inner">
            <h1>Clear Polybags with Suffocation Warning in 10 languages <br> Ideal for Amazon FBA</h1>
            <div type="button" class="btn-primary btn shopNowBtn">
                <a class="shopNow" href="${pageContext.request.contextPath}/collection">SHOP NOW</a>
            </div>
        </div>
    </div>
</div>
<div id="transparentBasket">
</div>
<div id="Basket">
    <div class="continueShopping">
        <p class="inlineblock">Continue Shopping</p> <i class="fa fa-chevron-right inlineblock"></i>
    </div>    <div  id="basketTitle"> 
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

<!-- The Modal -->
<!--<div id="myModal" class="modalMe">-->

<!-- Modal content -->
<!--<div class="modal-content">       
    <h2>Some must knows before you shop:</h2>
    <ul>
        <li>This is a superior range of strong, lightweight, clear polythene bags which are designed to provide quality, security and performance that you can trust.</li>
        <li>The high specification of the clear film has been chosen to make it easy to bag an item, wrap it around and then easily seal it with the permanent sealing strip. </li>
        <li>Use them for Books, Video Games, CDs, DVDs, Board Games and Toys, Gifts, etc.</li>
        <li>The bag has a 50mm lip with a permanent self-seal strip to fold over and seal the contents, and a suffocation warning in 10 languages.</li>
        <li>The bags are clear, strong (140 Gauge/ 35 Micron) LDPE Poly Bags available in 4 sizes, either in quantities of 100 or 1000.</li>
        <li>If you’re not sure which one to choose, then why not select one of the 3 sets of mixed bags featuring 100 (25 of each size), 400 (100 of each size) or 1000 (250 of each size) bags.</li>
    </ul>    
    <div class='close'>
        <span>Let me in!</span>
    </div>
</div>


</div>-->
<div id="cookieConsent">
    <div id="closeCookieConsent">x</div>
    This website is using cookies. <a href="${pageContext.request.contextPath}/ContactUs" target="_blank">More info</a>. <a class="cookieConsentOK">That's Fine</a>
</div>
<jsp:include page="_footer.jsp"></jsp:include>
<script src="JS/storeAndBasket.js"></script>
</body>
</html>
