package com.polybags.utils;

import com.polybags.beans.Discount;
import com.polybags.beans.Email;
import com.polybags.beans.Order;
import com.polybags.beans.Product;
import java.sql.Connection;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.polybags.beans.UserAccount;
import com.squareup.square.SquareClient;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.CreatePaymentResponse;
import com.squareup.square.models.Money;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MyUtils {

    public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";
    private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

    // Store Connection in request attribute.
    // (Information stored only exist during requests)
    public static void storeConnection(ServletRequest request, Connection conn) {
        request.setAttribute(ATT_NAME_CONNECTION, conn);
    }

    // Get the Connection object has been stored in attribute of the request.
    public static Connection getStoredConnection(ServletRequest request) {
        Connection conn = (Connection) request.getAttribute(ATT_NAME_CONNECTION);
        return conn;
    }

    // Store user info in Session.
    public static void storeLoginedUser(HttpSession session, UserAccount loginedUser) {
        // On the JSP can access via ${loginedUser}
        session.setAttribute("loginedUser", loginedUser);
    }

    // Get the user information stored in the session.
    public static UserAccount getLoginedUser(HttpSession session) {
        UserAccount loginedUser = (UserAccount) session.getAttribute("loginedUser");
        return loginedUser;
    }

    // Store info in Cookie
    public static void storeUserCookie(HttpServletResponse response, UserAccount user) {
        System.out.println("Store user cookie");
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getUserName());
        // 1 day (Converted to seconds)
        cookieUserName.setMaxAge(24 * 60 * 60);
        response.addCookie(cookieUserName);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    public static String getUserNameInCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ATT_NAME_USER_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Delete cookie.
    public static void deleteUserCookie(HttpServletResponse response) {
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, null);
        // 0 seconds (This cookie will expire immediately)
        cookieUserName.setMaxAge(0);
        response.addCookie(cookieUserName);
    }

    public static String generateBasketId(Connection conn) {
        int basketIdInt = 0;
        try {
            basketIdInt = DBUtils.generateBasketIdNumber(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        String basketId = generatedString + "-" + (basketIdInt + 1) + "";

        return basketId;
    }

    public static Map<String, Object> charge(Connection conn, String nonce, Long amount, String currency, String idempotency_key, String orderNo, SquareClient squareClient) throws ApiException, IOException {
        // To learn more about splitting payments with additional recipients,
        // see the Payments API documentation on our [developer site]
        // (https://developer.squareup.com/docs/payments-api/overview).
        Map<String, Object> model = new HashMap<>();
        Integer responseStatus = 0;

        Money bodyAmountMoney = new Money.Builder()
                .amount(amount)
                .currency(currency)
                .build();
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest.Builder(
                nonce,
                UUID.randomUUID().toString(),
                bodyAmountMoney)
                .autocomplete(true).idempotencyKey(idempotency_key)
                .note("Order No: " + orderNo)
                .build();

        PaymentsApi paymentsApi = squareClient.getPaymentsApi();

        try {
            CreatePaymentResponse response = paymentsApi.createPayment(createPaymentRequest);
            model.put("payment", response.getPayment());
            responseStatus = 200;
            model.put("response", responseStatus);
            model.put("orderNo", orderNo);
            return model;
        } catch (ApiException except) {
            responseStatus = 500;

            com.squareup.square.models.Error error = except.getErrors().get(0);
            model.put("error", except.getErrors().get(0));
            model.put("response", responseStatus);

            return model;
        }
    }

    public static Boolean sendOrderEmailToCustomer(Order order) throws javax.mail.MessagingException {
        String to = order.getEmail();

        // Sender's email ID needs to be mentioned
        String from = "mark@justbagit.co.uk";
        final String username = "mark@justbagit.co.uk";//change accordingly
        final String password = "MarkC1eary123!";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "localhost";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        // Set From: header field of the header.
        message.setFrom(from);
        // Set To: header field of the header.
        message.setRecipients(RecipientType.TO,
                InternetAddress.parse(to));
        // Set Subject: header field
        message.setSubject("Justbagit.co.uk Purchase Order #" + order.getPOId());
        List<Product> products = order.getProducts();
        String deliveryMethod;
        if(order.getLeadTime() == 1){
            deliveryMethod = "Next Working Day";
        }else{
            deliveryMethod = "Second Class(3-5 Working Days)";
        }
        String cartItemsHTML = "";
        for (Product item : products) {
            String itemHTML = "<div class=\"confirmation-cart-item\">"
                    + "<div class=\"confirmation-cart-item-title\">"
                    + "   <h4 style=\"color: grey;\">" + item.getSize() + "</h4>"
                    + "   <h5 class=\"confirmation-cart-item-price\"><strong>&pound;" + item.getPrice() + "</strong></h5>"
                    + "   <h5 class=\"cart-item-per\"><strong>Per: " + item.getPer() + "</strong></h5>"
                    + "   <h5 class=\"confirmation-qty\">Qty: <strong>" + item.getQty() + "</strong></h5>"
                    + "</div>"
                    + "</div>";
            cartItemsHTML += itemHTML;
        }
        message.setContent(
                "<html>\n"
                + "    <head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <link rel=\"stylesheet\" href=\"https://s3.amazonaws.com/codecademy-content/courses/ltp/css/bootstrap.css\">\n"
                + "        <title>Order Confirmation</title>\n"
                + "<style>"
                + ".orderConfirmationHeader{\n"
                + "    text-align: center;\n"
                + "}\n"
                + ".confirmation-cart-item-image img{\n"
                + "    height: 125px;\n"
                + "    width: 125px;\n"
                + "}"
                + "\n"
                + ".Deliverydetails, .orderConfirmationProducts{\n"
                + "    margin: 10px auto;\n"
                + "    width: 60%;\n"
                + "    font-family: FuturaPTBook-Reg, Futura, Arial, sans-serif;\n"
                + "}\n"
                + "\n"
                + ".deliveryDetailsConfirmationTitle{\n"
                + "    border-bottom: 1px solid #eeeeee;\n"
                + "    padding: 5px;\n"
                + "    height: 55px;\n"
                + "}\n"
                + "\n"
                + ".deliveryDetailsConfirmationContent h5, .deliveryMethodConfirmation h5{\n"
                + "    color: #767676;\n"
                + "    padding: 5px;\n"
                + "    margin-top: 30px;\n"
                + "}\n"
                + "\n"
                + ".confirmationContent{\n"
                + "    padding: 5px;\n"
                + "}"
                + "</style>"
                + "    </head>\n"
                + "    <body>\n"
                + "        <div class=\"orderConfirmationHeader\">\n"
                + "            <h3 class=\"Ordered\"><strong>IT'S ORDERED!</strong></h3>\n"
                + "            <p>Hi <span class=\"name\">" + order.getCustomerName() + "</span> your order has been received.</p>\n"
                + "            <table>\n"
                + "                <tr><p>Order No.:<span id=\"orderNumber\">" + order.getPOId() + "</span></p></tr>\n"
                + "                <tr><p>Order Date:<span id=\"orderDate\">" + order.getOrderSent() + "</span></p></tr>\n"
                + "                <tr><p Style=\"color:#006e3b\">Estimated Delivery Date:<span id=\"estimatedDeliveryDate\">" + order.getDueDate() + "</span><br><b>Please Note:</b> The estimated delivery date does not take into account weekends or bank holidays. <br>In any case this will be the next working day.</p></tr>\n"
                + "            </table>\n"
                + "        </div>\n"
                + "        <div class=\"card Deliverydetails\">\n"
                + "            <div class=\"deliveryDetailsConfirmationTitle\">\n"
                + "                <h4><strong>DELIVERY DETAILS</strong></h4>\n"
                + "            </div>\n"
                + "            <div class=\"deliveryDetailsConfirmationContent\">\n"
                + "                <h5><strong>DELIVERY ADDRESS</strong></h5>\n"
                + "                <div class=\"confirmationContent\">\n"
                + "                    <p><span class=\"name\">" + order.getCustomerName() + "</span></p>\n"
                + "                    <p><span id=\"firstLineAddress\">" + order.getShipToStreet1() + "</span></p>\n"
                + "                    <p><span id=\"secondLineAddress\">" + order.getShipToStreet2() + "</span></p>\n"
                + "                    <p><span id=\"county\">" + order.getCounty() + "</span></p>\n"
                + "                    <p><span id=\"country\">" + order.getCountry() + "</span></p>\n"
                + "                    <p><span id =\"postcode\">" + order.getShipToPostcode() + "</span></p>\n"
                + "                </div>\n"
                + "            </div>\n"
                + "            <div class=\"deliveryMethodConfirmation\">\n"
                + "                <h5><strong>DELIVERY METHOD</strong></h5>\n"
                + "                <div class=\"deliveryConfirmationContent\">\n"
                + "                    <p><span id=\"deliveryMethodInformation\">"+deliveryMethod+"</span></p>\n"
                + "                </div>\n"
                + "            </div>\n"
                + "        </div>\n"
                + "        <div class=\"card orderConfirmationProducts\">\n"
                + "            <div class=\"orderConfirmationBasket\">\n"
                + "                <div class=\"orderConfirmationBasketTitle\"><h4><strong>Invoice and delivery statement</strong></h4></div>\n"
                + "                <div class=\"orderConfirmationBasketContent\"> "
                + cartItemsHTML
                + "                </div>\n"
                + "                <div class=\"orderConfirmationTotals\">\n"
                + "                    <div class=\"orderConfirmationSubtotal totalGrid\">\n"
                + "                        <div>\n"
                + "                            <h5>Subtotal</h5>\n"
                + "                        </div>\n"
                + "                        <div>\n"
                + "                            <span class=\"subTotalAmount\" id=\"subtotalAmount\">&pound;" + order.getSubtotal() + "</span>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "                    <div class=\"discountCheckout notVisible totalGrid\">\n"
                + "                    </div>\n"
                + "                    <div class=\"orderConfirmationDeliveryMethod totalGrid\">\n"
                + "                        <div>\n"
                + "                            <h5>Delivery</h5>\n"
                + "                        </div>\n"
                + "                        <div>\n"
                + "                            <span id=\"orderConfirmationDeliveryPrice\">&pound;" + order.getDeliveryPrice() + "</span>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "                    <div class=\"orderConfirmationTotal totalGrid\">\n"
                + "                        <div>\n"
                + "                            <h4>\n"
                + "                                <strong>Paid</strong>\n"
                + "                            </h4>\n"
                + "                        </div>\n"
                + "                        <div><span class=\"totalAmount\" id=\"totalAmount\">&pound;" + order.getTotal() + "</span>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "                    \n"
                + "                </div>               \n"
                + "            </div>\n"
                + "            \n"
                + "        </div>\n"
                + "        </script>\n"
                + "    </body>\n"
                + "</html>",
                "text/html");
        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully....");

        return true;
    }
    
    public static Boolean sendOrderEmailToCompany(Order order) throws javax.mail.MessagingException {
        String to = "mark@justbagit.co.uk";

        // Sender's email ID needs to be mentioned
        String from = "mark@justbagit.co.uk";
        final String username = "mark@justbagit.co.uk";//change accordingly
        final String password = "MarkC1eary123!";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "localhost";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        // Set From: header field of the header.
        message.setFrom(from);
        // Set To: header field of the header.
        message.setRecipients(RecipientType.TO,
                InternetAddress.parse(to));
        // Set Subject: header field
        message.setSubject("Kerching! New Purchase Order #" + order.getPOId() +" justbagit.co.uk");
        List<Product> products = order.getProducts();
        String deliveryMethod;
        if(order.getLeadTime() == 1){
            deliveryMethod = "Next Working Day";
        }else{
            deliveryMethod = "Second Class(3-5 Working Days)";
        }
        
        String cartItemsHTML = "";
        for (Product item : products) {
            String itemHTML = "<div class=\"confirmation-cart-item\">"
/*                    + "<div class=\"confirmation-cart-item-image\">"
                    + "<img class=\"image\" src=\"https://justbagit.co.uk/Images/" + item.getImage() + ">"
                    + "</div>"*/
                    + "<div class=\"confirmation-cart-item-title\">"
                    + "   <h4 style=\"color: grey;\">" + item.getSize() + "</h4>"
                    + "   <h5 class=\"confirmation-cart-item-price\"><strong>&pound;" + item.getPrice() + "</strong></h5>"
                    + "   <h5 class=\"cart-item-per\"><strong>Per: " + item.getPer() + "</strong></h5>"
                    + "   <h5 class=\"confirmation-qty\">Qty: <strong>" + item.getQty() + "</strong></h5>"
                    + "</div>"
                    + "</div>";
            cartItemsHTML += itemHTML;
        }
        message.setContent(
                "<html>\n"
                + "    <head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <link rel=\"stylesheet\" href=\"https://s3.amazonaws.com/codecademy-content/courses/ltp/css/bootstrap.css\">\n"
                + "        <title>Order Confirmation</title>\n"
                + "<style>"
                + ".orderConfirmationHeader{\n"
                + "    text-align: center;\n"
                + "}\n"
                + ".confirmation-cart-item-image img{\n"
                + "    height: 125px;\n"
                + "    width: 125px;\n"
                + "}"
                + "\n"
                + ".Deliverydetails, .orderConfirmationProducts{\n"
                + "    margin: 10px auto;\n"
                + "    width: 60%;\n"
                + "    font-family: FuturaPTBook-Reg, Futura, Arial, sans-serif;\n"
                + "}\n"
                + "\n"
                + ".deliveryDetailsConfirmationTitle{\n"
                + "    border-bottom: 1px solid #eeeeee;\n"
                + "    padding: 5px;\n"
                + "    height: 55px;\n"
                + "}\n"
                + "\n"
                + ".deliveryDetailsConfirmationContent h5, .deliveryMethodConfirmation h5{\n"
                + "    color: #767676;\n"
                + "    padding: 5px;\n"
                + "    margin-top: 30px;\n"
                + "}\n"
                + "\n"
                + ".confirmationContent{\n"
                + "    padding: 5px;\n"
                + "}"
                + "</style>"
                + "    </head>\n"
                + "    <body>\n"
                + "        <div class=\"orderConfirmationHeader\">\n"
                + "            <h4 class=\"Ordered\"><strong>Mark, Fantastic News! You have a new order:</strong></h4>\n"
                + "            <p>Hi <span class=\"name\">" + order.getCustomerName() + "</span> your order has been received.</p>\n"
                + "            <table>\n"
                + "                <tr><p>Order No.:<span id=\"orderNumber\">" + order.getPOId() + "</span></p></tr>\n"
                + "                <tr><p>Order Date:<span id=\"orderDate\">" + order.getOrderSent() + "</span></p></tr>\n"
                + "                <tr><p Style=\"color:#006e3b\">Estimated Delivery Date:<span id=\"estimatedDeliveryDate\">" + order.getDueDate() + "</span></p></tr>\n"
                + "            </table>\n"
                + "        </div>\n"
                + "        <div class=\"card Deliverydetails\">\n"
                + "            <div class=\"deliveryDetailsConfirmationTitle\">\n"
                + "                <h4><strong>DELIVERY DETAILS</strong></h4>\n"
                + "            </div>\n"
                + "            <div class=\"deliveryDetailsConfirmationContent\">\n"
                + "                <h5><strong>CUSTOMER'S ADDRESS</strong></h5>\n"
                + "                <div class=\"confirmationContent\">\n"
                + "                    <p><span class=\"name\">" + order.getCustomerName() + "</span></p>\n"
                +"                      <p><span>"+order.getEmail()+"</span></p>\n"
                + "                    <p><span id=\"firstLineAddress\">" + order.getShipToStreet1() + "</span></p>\n"
                + "                    <p><span id=\"secondLineAddress\">" + order.getShipToStreet2() + "</span></p>\n"
                + "                    <p><span id=\"county\">" + order.getCounty() + "</span></p>\n"
                + "                    <p><span id=\"country\">" + order.getCountry() + "</span></p>\n"
                + "                    <p><span id =\"postcode\">" + order.getShipToPostcode() + "</span></p>\n"
                + "                </div>\n"
                + "            </div>\n"
                + "            <div class=\"deliveryMethodConfirmation\">\n"
                + "                <h5><strong>DELIVERY METHOD</strong></h5>\n"
                + "                <div class=\"deliveryConfirmationContent\">\n"
                + "                    <p><span id=\"deliveryMethodInformation\">"+deliveryMethod+"</span></p>\n"
                + "                </div>\n"
                + "            </div>\n"
                + "        </div>\n"
                + "        <div class=\"card orderConfirmationProducts\">\n"
                + "            <div class=\"orderConfirmationBasket\">\n"
                + "                <div class=\"orderConfirmationBasketTitle\"><h4><strong>Customer's Invoice and delivery statement</strong></h4></div>\n"
                + "                <div class=\"orderConfirmationBasketContent\"> "
                + cartItemsHTML
                + "                </div>\n"
                + "                <div class=\"orderConfirmationTotals\">\n"
                + "                    <div class=\"orderConfirmationSubtotal totalGrid\">\n"
                + "                        <div>\n"
                + "                            <h5>Subtotal</h5>\n"
                + "                        </div>\n"
                + "                        <div>\n"
                + "                            <span class=\"subTotalAmount\" id=\"subtotalAmount\">&pound;" + order.getSubtotal() + "</span>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "                    <div class=\"discountCheckout notVisible totalGrid\">\n"
                + "                    </div>\n"
                + "                    <div class=\"orderConfirmationDeliveryMethod totalGrid\">\n"
                + "                        <div>\n"
                + "                            <h5>Delivery</h5>\n"
                + "                        </div>\n"
                + "                        <div>\n"
                + "                            <span id=\"orderConfirmationDeliveryPrice\">&pound;" + order.getDeliveryPrice() + "</span>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "                    <div class=\"orderConfirmationTotal totalGrid\">\n"
                + "                        <div>\n"
                + "                            <h4>\n"
                + "                                <strong>Total Customer has Paid </strong>\n"
                + "                            </h4>\n"
                + "                        </div>\n"
                + "                        <div><span class=\"totalAmount\" id=\"totalAmount\">&pound;" + order.getTotal() + "</span>\n"
                + "                        </div>\n"
                + "                    </div>\n"
                + "                    \n"
                + "                </div>               \n"
                + "            </div>\n"
                + "            \n"
                + "        </div>\n"
                + "        </script>\n"
                + "    </body>\n"
                + "</html>",
                "text/html");
        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully....");

        return true;
    }

    public static Boolean sendEmailTojustbagit(Email email) throws AddressException, javax.mail.MessagingException {
        String to = "mark@justbagit.co.uk";

        // Sender's email ID needs to be mentioned
        String from = email.getEmail();
        final String username = "mark@justbagit.co.uk";//change accordingly
        final String password = "MarkC1eary123!";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "localhost";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);
        // Set From: header field of the header.
        message.setFrom(from);
        // Set To: header field of the header.
        message.setRecipients(RecipientType.TO,
                InternetAddress.parse(to));
        // Set Subject: header field
        message.setSubject(email.getSubject());
        // Now set the actual message
        message.setContent(email.getFirstname() + " " + email.getLastname()+"<br><br>"+email.getMessage(),"text/html");
        // Send message
        Transport.send(message);

        return true;

    }

    public static Map<String, BigDecimal> calculateProductTotals(Connection conn, List<Product> products, BigDecimal subtotal, BigDecimal total) {
        Map<String, BigDecimal> totals = new HashMap<>();
        try {
            HashMap<String, Discount> discounts = DBUtils.getDiscounts(conn);

            int itemCount = 0;
            for (Product product : products) {
                subtotal = subtotal.add(product.calculateSubtotal());
                total = total.add(product.calculateTotal(conn));
                itemCount += product.getQty();
            }
            if (itemCount >= discounts.get("lowerDiscount").getPer() && itemCount < discounts.get("upperDiscount").getPer()) {
                total = subtotal.multiply((BigDecimal.valueOf(100).subtract(BigDecimal.valueOf((discounts.get("lowerDiscount").getDiscount()))).divide(BigDecimal.valueOf(100))));
            }
            if (itemCount >= discounts.get("upperDiscount").getPer()) {
                total = subtotal.multiply((BigDecimal.valueOf(100).subtract(BigDecimal.valueOf((discounts.get("upperDiscount").getDiscount()))).divide(BigDecimal.valueOf(100))));
            }

            totals.put("subtotal", subtotal);
            totals.put("total", total);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totals;
    }

    public static int calculateOrderLeadTime(List<Product> products, int leadtime) {

        for (Product product : products) {
            if (leadtime > product.getLeadTime()) {
                leadtime = product.getLeadTime();
            }
        }

        return leadtime;
    }
}
