<%-- 
    Document   : orderConfirmationView
    Created on : 24-Jan-2021, 16:41:14
    Author     : bencleary
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="_checkoutHeader.jsp"></jsp:include>  

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="CSS/stylesheet.css">
        <link rel="stylesheet" href="https://s3.amazonaws.com/codecademy-content/courses/ltp/css/bootstrap.css">  
        <link type="text/css" rel="icon" type="image/gif/png" href="Images/icon.png">
        <link rel="stylesheet" href="CSS/font-awesome-4.7.0/css/font-awesome.min.css">
        <title>Order Confirmation</title>
    </head>
    <body>
        <div class="orderConfirmationHeader">
            <h3 class="Ordered"><strong>IT'S ORDERED!</strong></h3>
            <p>Hi <span class="name"></span>your order has been received.</p>
            <table>
                <tr><p>Order No.:<span id="orderNumber"></span></p></tr>
                <tr><p>Order Date:<span id="orderDate"></span></p></tr>
                <tr><p Style="color:#006e3b">Estimated Delivery Date:<span id="estimatedDeliveryDate"></span><br><b>Please Note:</b> The estimated delivery date does not take into account weekends or bank holidays. <br>In any case this will be the next working day.</p></tr>
            </table>
        </div>
        <div class="card Deliverydetails">
            <div class="deliveryDetailsConfirmationTitle">
                <h4><strong>DELIVERY DETAILS</strong></h4>
            </div>
            <div class="deliveryDetailsConfirmationContent">
                <h5><strong>DELIVERY ADDRESS</strong></h5>
                <div class="confirmationContent">
                    <p><span class="name"></span></p>
                    <p><span id="firstLineAddress"></span></p>
                    <p><span id="secondLineAddress"></span></p>
                    <p><span id="county"></span></p>
                    <p><span id="country"></span></p>
                    <p><span id ="postcode"></span></p>
                    <p><span id="telephone"></span></p>
                </div>
            </div>
            <div class="deliveryMethodConfirmation">
                <h5><strong>DELIVERY METHOD</strong></h5>
                <div class="deliveryConfirmationContent">
                    <p><span id="deliveryMethodInformation"></span></p>
                </div>
            </div>
            <div class="notesConfirmation">
                <h5><strong>NOTES</strong></h5>
                <p><span id="notes"></span></p>
            </div>

        </div>
        <div class="card orderConfirmationProducts">
            <div class="orderConfirmationBasket">
                <div class="orderConfirmationBasketTitle"><h4><strong>My Basket</strong></h4></div>
                <div class="orderConfirmationBasketContent">                   
                </div>
                <div class="orderConfirmationTotals">
                    <div class="orderConfirmationSubtotal totalGrid">
                        <div>
                            <h5>Subtotal</h5>
                        </div>
                        <div>
                            <span class="subTotalAmount" id="subtotalAmount"></span>
                        </div>
                    </div>
                    <!--                    <div class="discountCheckout notVisible totalGrid">
                    
                                        </div>-->
                    <div class="quantityDiscountCheckout notVisible totalGrid">

                    </div>
                    <div class="promocodeCheckout notVisible totalGrid">

                    </div>
                    <div class="orderConfirmationDeliveryMethod totalGrid">
                        <div>
                            <h5>Delivery</h5>
                        </div>
                        <div>
                            <span id="orderConfirmationDeliveryPrice"></span>
                        </div>
                    </div>
                    <div class="orderConfirmationTotal totalGrid">
                        <div>
                            <h4>
                                <strong>Paid </strong>
                            </h4>
                        </div>
                        <div><span class="totalAmount" id="totalAmount"></span>
                        </div>
                    </div>

                </div>               
            </div>

        </div>
        <script type="text/javascript" src="https://s3.amazonaws.com/codecademy-content/projects/jquery.min.js">
        </script>
        <script src="https://code.jquery.com/jquery-1.10.2.js"></script>

        <script src="JS/orderConfirmation.js"></script>
    </body>
</html>
