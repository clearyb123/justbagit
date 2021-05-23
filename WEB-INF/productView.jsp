<%-- 
    Document   : productView.jsp
    Created on : 12-Mar-2021, 19:29:07
    Author     : bencleary
--%>

<jsp:include page="_header.jsp"></jsp:include>  
    <div class="product_Page_Content">
        <div class="container">
            <div class="content_product_details">
                <div class="image_selection">
                    <div class="main_product_image img-magnifier-container">
                        <img id="main_image" src="" alt="main_image">
                    </div>
                    <div class="image_row">
                        <div class="secondary_images">
                            <img id="secondary_image_first" class="secondary_product_images" src="" alt="secondary_image">
                            <img id="secondary_image_second" class="secondary_product_images" src="" alt="secondary_image">
                            <img id="secondary_image_third" class="secondary_product_images" src="" alt="secondary_image">
                            <img id="secondary_image_fourth" class="secondary_product_images" src="" alt="secondary_image">
                        </div>
                    </div>
                </div>
                <div class="productDetails">
                    <div class="product_Description">
                        <h2 id="product_Title"><b><span id="product_Title_Span"></span></b></h2>
                        <h4><span style="color:#999999" id="product_description"></span></h4>
                    </div>
                    <div class="product_Price">
                        <h4><b>£<span id="product_Price_Span"></span></b></h4>
                    </div>
                    <div class="per_Dropdown">

                    </div>
                    <div class="Quantity">

                    </div>
                    <div class="addToBasketContainer">  

                    </div>
                    <div class="product_Description_Outlier">

                    </div>
                </div>
            </div>
        </div>

        <div class="hero_small">
            <div class="hero_small_inner ">
                <h3>More like this</h3>
                <div class="products">
                    <div class="containerS"></div>
                </div>                
            </div>
        </div>
        <div class="review_Hero">
            <div class="review_Hero_Describe">

            </div>
            <div class="review_stars">

            </div>
            <div class="review_form">

            </div>
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
            <button class="checkout-cart banner-btn"><b>CHECKOUT</b></button>
            <p style="color: black;font: 10px;">Items are reserved for 24 hours</p>
        </div>
    </div>

<jsp:include page="_footer.jsp"></jsp:include>  
<script src="JS/storeAndBasket.js"></script>

</body>
</html>
