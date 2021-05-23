<%-- 
    Document   : checkOutView
    Created on : 10-Jan-2021, 20:19:41
    Author     : bencleary
--%>
<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.json.*" %>
<jsp:include page="_checkoutHeader.jsp"></jsp:include>  
    <html>
        <head>
            <title>Polybags CheckOut</title>
<!--            <script type="text/javascript" src="https://js.squareupsandbox.com/v2/paymentform">
            </script>-->
            <script type="text/javascript" src="https://js.squareup.com/v2/paymentform">
            </script>
            <link rel="stylesheet" type="text/css" href="CSS/mysqpaymentform.css">
        </head>
        <body id="checkoutBody">
            <div class="checkoutContentBackground">
                <div class="checkoutContent">
                    <div class="checkoutInformationContent">  
                        <div class="card emailAddressContent">

                            <h3>Email Address:<span style="color:red">*</span></h3>
                            <p class="validationError" id="emailValidationError"></p>

                            <input name="emailAddress" id="emailAddress" placeholder="Enter your Email Address">
                        </div>
                        <div class="card deliveryOptionsContent">
                            <h3>Choose a Delivery Method:</h3>
                            <div class="deliveryMethodContent">

                            </div>
                        </div>   
                        <div class="card deliveryAddressContent">
                            <h2>Shipping</h2>
                            <p>Please enter your shipping details.</p>
                            <hr/>
                            <div class="form">
                                <div class="field_fN">
                                    <h4>First Name<span style="color:red">*</span></h4>
                                    <p class="validationError" id="firstNameValidationError"></p>
                                    <input type="text" class="addressInput" name="firstName"  id="firstName" placeholder="First name">
                                </div>
                                <div class="field_lN">
                                    <h4>Last Name<span style="color:red">*</span></h4>
                                    <p class="validationError" id="lastNameValidationError"></p>

                                    <input type="text" class="addressInput" name="lastName" id="lastName" placeholder="Last name">
                                </div>
                                <div class="field_addressLine1">
                                    <h4>Delivery Address<span style="color:red">*</span></h4>
                                    <p class="validationError" id="firstLineAddressValidationError"></p>
                                    <input type="text" class="addressInput" name="firstLineAddress" id="firstLineAddress" placeholder="Address Line 1">
                                    <input type="text" class="addressInput" name="secondLineAddress" id="secondLineAddress" placeholder="Address Line 2 (optional)">
                                    <input type="text" class="addressInput" name="thirdLineAddress" id="thirdLineAddress" placeholder="Address Line 3 (optional)">
                                    <input type="text" class="addressInput" name="city" id="city" placeholder="City">
                                    <input type="text" class="addressInput" name="county" id="county" placeholder="County/ State (optional)">
                                    <p class="validationError" id="postcodeValidationError"></p>

                                    <input type="text" class="addressInput" name="postalcode" id="postalcode" placeholder="Postal Code">
                                    <p class="validationError" id="telephoneValidationError"></p>
                                    <input type="text" class="addressInput" name="telephone" id="telephone" placeholder="Telephone Number">
                                </div>
                            </div>
                        </div>
                        <div class="card deliveryCountryContent">
                            <h5>Delivery Country:</h5>
                            <p class="validationError" id="countryValidationError"></p>
                            <select name="country" class="form-control" id="country">
                                <option value="0" label="Select a country ... " selected="selected">Select a country ... </option>


                                <optgroup id="country-optgroup-Europe" label="Europe">
                                    <!--                                <option value="AL" label="Albania">Albania</option>
                                                                    <option value="AD" label="Andorra">Andorra</option>
                                                                    <option value="AT" label="Austria">Austria</option>
                                                                    <option value="BY" label="Belarus">Belarus</option>
                                                                    <option value="BE" label="Belgium">Belgium</option>
                                                                    <option value="BA" label="Bosnia and Herzegovina">Bosnia and Herzegovina</option>
                                                                    <option value="BG" label="Bulgaria">Bulgaria</option>
                                                                    <option value="HR" label="Croatia">Croatia</option>
                                                                    <option value="CY" label="Cyprus">Cyprus</option>
                                                                    <option value="CZ" label="Czech Republic">Czech Republic</option>
                                                                    <option value="DK" label="Denmark">Denmark</option>
                                                                    <option value="DD" label="East Germany">East Germany</option>
                                                                    <option value="EE" label="Estonia">Estonia</option>
                                                                    <option value="FO" label="Faroe Islands">Faroe Islands</option>
                                                                    <option value="FI" label="Finland">Finland</option>
                                                                    <option value="FR" label="France">France</option>
                                                                    <option value="DE" label="Germany">Germany</option>
                                                                    <option value="GI" label="Gibraltar">Gibraltar</option>
                                                                    <option value="GR" label="Greece">Greece</option>
                                                                    <option value="GG" label="Guernsey">Guernsey</option>
                                                                    <option value="HU" label="Hungary">Hungary</option>
                                                                    <option value="IS" label="Iceland">Iceland</option>
                                                                    <option value="IE" label="Ireland">Ireland</option>
                                                                    <option value="IM" label="Isle of Man">Isle of Man</option>
                                                                    <option value="IT" label="Italy">Italy</option>
                                                                    <option value="JE" label="Jersey">Jersey</option>
                                                                    <option value="LV" label="Latvia">Latvia</option>
                                                                    <option value="LI" label="Liechtenstein">Liechtenstein</option>
                                                                    <option value="LT" label="Lithuania">Lithuania</option>
                                                                    <option value="LU" label="Luxembourg">Luxembourg</option>
                                                                    <option value="MK" label="Macedonia">Macedonia</option>
                                                                    <option value="MT" label="Malta">Malta</option>
                                                                    <option value="FX" label="Metropolitan France">Metropolitan France</option>
                                                                    <option value="MD" label="Moldova">Moldova</option>
                                                                    <option value="MC" label="Monaco">Monaco</option>
                                                                    <option value="ME" label="Montenegro">Montenegro</option>
                                                                    <option value="NL" label="Netherlands">Netherlands</option>
                                                                    <option value="NO" label="Norway">Norway</option>
                                                                    <option value="PL" label="Poland">Poland</option>
                                                                    <option value="PT" label="Portugal">Portugal</option>
                                                                    <option value="RO" label="Romania">Romania</option>
                                                                    <option value="RU" label="Russia">Russia</option>
                                                                    <option value="SM" label="San Marino">San Marino</option>
                                                                    <option value="RS" label="Serbia">Serbia</option>
                                                                    <option value="CS" label="Serbia and Montenegro">Serbia and Montenegro</option>
                                                                    <option value="SK" label="Slovakia">Slovakia</option>
                                                                    <option value="SI" label="Slovenia">Slovenia</option>
                                                                    <option value="ES" label="Spain">Spain</option>
                                                                    <option value="SJ" label="Svalbard and Jan Mayen">Svalbard and Jan Mayen</option>
                                                                    <option value="SE" label="Sweden">Sweden</option>
                                                                    <option value="CH" label="Switzerland">Switzerland</option>
                                                                    <option value="UA" label="Ukraine">Ukraine</option>
                                                                    <option value="SU" label="Union of Soviet Socialist Republics">Union of Soviet Socialist Republics</option>-->
                                    <option value="UK" label="United Kingdom">United Kingdom</option>
                                    <!--<option value="VA" label="Vatican City">Vatican City</option>
                                    <option value="AX" label="Åland Islands">Åland Islands</option>-->
                                </optgroup>

                            </select>
                            <p>We currently only ship to the UK. We are sorry for any inconvenience caused. <br>We hope to be shipping to countries in the EU soon!</p>
                        </div>
                        <div class="card notesContent">
                            <h4>Notes:</h4>
                            <textarea type="text" maxlength="200" class="notes" name="notes" id="notes" placeholder="Please add your delivery notes here..."></textarea>
                        </div>
                        
                        <div class="card paymentContent">
                            <h1>Payment Method</h1>
                            <div class="billingAddress">
                                <h4>Billing Address</h4>
                                <label><span>Same as Delivery Address</span>
                                    <input type="checkbox" name="sameAsShippingAddress" checked id="sameAsShippingAddress">
                                </label>
                                <div class="billingAddressContent">
                                    <p class="validationError" id="billingFirstLineAddressValidationError"></p>
                                    <input type="text" class="addressInput displayNot billingAddressInput" name="billingFirstLineAddress" id="billingFirstLineAddress" placeholder="Address Line 1">
                                    <input type="text" class="addressInput displayNot billingAddressInput" name="billingSecondLineAddress" id="billingSecondLineAddress" placeholder="Address Line 2">
                                    <input type="text" class="addressInput displayNot billingAddressInput" name="billingThirdLineAddress" id="billingThirdLineAddress" placeholder="Address Line 3">
                                    <input type="text" class="addressInput displayNot billingAddressInput" name="billingCity" id="billingCity" placeholder="City">
                                    <input type="text" class="addressInput displayNot billingAddressInput" name="billingCounty" id="billingCounty" placeholder="County/ State">                                                       
                                    <p class="validationError" id="billingPostcodeValidationError"></p>
                                </div>
                            </div>
                            <div class="paymentDetails">
                                <h4>Payment Details:</h4>
                                <p class="validationError" id="ccValidationError"></p>

                                <div id="form-container">
                                    <div id="sq-card-number"></div>
                                    <div class="third" id="sq-expiration-date"></div>
                                    <div class="third" id="sq-cvv"></div>
                                    <div class="third" id="sq-postal-code"></div>
                                    <i id="postalcodeinfo" title="This form is built in collaboration with Square. They currently require you to input your postal code here." class="fa fa-info-circle"><span title="Square currently requires your post code"></span></i>
                                </div>
                            </div>
                        </div>
                        <div class="placeOrder">
                            <button id="sq-creditcard" class="placeOrderButton button-credit-card"><strong><span class="display placeOrderText">PLACE ORDER</span></strong></button>

                            <!--<button class="placeOrderButton btn primary"><strong>PLACE ORDER</strong></button>-->
                            <p class="validationError" id="masterValidationError"></p>
                            <div class=termsAndConditions">
                                <h4 style="color: #666">By placing your order you agree to our Terms & Conditions, privacy and returns policies.</h4>
                            </div> 
                        </div>
                    </div>
                    <div class="checkoutBasket">
                        <div class="checkoutBasketTitle"><h4><strong>My Basket</strong></h4></div>
                        <div class="checkoutBasketContent">                   
                        </div>
                        <div class="checkoutTotals">
                            <div class="checkoutSubtotal totalGrid">
                                <div>
                                    <h5>Subtotal</h5>
                                </div>
                                <div>
                                    <span class="subTotalAmount" id="subTotalAmount"></span>
                                </div>
                            </div>

                            <div class="discountCheckout notVisible totalGrid">

                            </div>
                        </div>

                        <div class="promocodeCheckout notVisible totalGrid">

                        </div>

                        <div class="checkoutDeliveryMethod totalGrid">
                            <div>
                                <h5>Delivery</h5>
                            </div>
                            <div>
                                <span id="checkoutDeliveryPrice"></span>
                            </div>
                        </div>
                        <div class="checkoutTotal totalGrid">
                            <div> <h4>
                                    <strong>Total To Pay</strong>
                                </h4></div>
                            <div>
                                <span class="totalAmount" id="totalAmount"></span>
                            </div>

                        </div>
                        <div class="promoCodeContent">
                            <h5><strong>Promo Code</strong></h5>
                            <div class='promocodeInput'>
                                <input name="promoCode" id="promoCodeInput" placeholder="Enter a promo code">
                                <button class="applyPromo btn primary">APPLY CODE</button>
                            </div>
                            <div class='promocodeSuccessMessage'>

                            </div>
                        </div>  
                    </div>

                </div>

            </div>
        </div>
    <jsp:include page="_footer.jsp"></jsp:include>

    <script src="JS/checkout.js" charset="ISO-8859-1"></script>

</body>
</html>
