/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.utils;

import com.polybags.beans.AuditHandler;
import com.polybags.beans.Cart;
import com.polybags.beans.Order;
import com.polybags.beans.Discount;
import com.polybags.beans.LeadTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import com.polybags.beans.Product;
import com.polybags.beans.PromoCodes;
import java.math.BigDecimal;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author bencleary
 */
public class DBUtils {

    public static Product getProduct(Connection conn, int id) throws SQLException {
        String sql = "Select Description, Size, Price, Per, Image, ImageTwo, ImageThree, ImageFour, Lead from Products where id = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, id);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            String description = rs.getString("Description");
            String size = rs.getString("Size");
            BigDecimal price = rs.getBigDecimal("Price");
            int per = rs.getInt("Per");
            String image = rs.getString("Image");
            String image2 = rs.getString("ImageTwo");
            String image3 = rs.getString("ImageThree");
            String image4 = rs.getString("ImageFour");

            int lead = rs.getInt("Lead");
            Product product = new Product();
            product.setDescription(description);
            product.setSize(size);
            product.setPrice(price);
            product.setPer(per);
            product.setImage(image);
            product.setImage2(image2);
            product.setImage3(image3);
            product.setImage4(image4);
            product.setId(id);
            product.setLeadTime(lead);

            return product;
        }
        return null;
    }

    public static List<Product> getProducts(Connection conn, String sessionId) throws SQLException {
        String sql = "Select Id, Description, Size, Price, Per, Image, Lead, EnableDisplay from Products";

        PreparedStatement pstm = conn.prepareStatement(sql);

        ResultSet rs = pstm.executeQuery();
        List<Product> list = new ArrayList<>();
        while (rs.next()) {
            String description = rs.getString("Description");
            String size = rs.getString("Size");
            BigDecimal price = rs.getBigDecimal("Price");
            int per = rs.getInt("Per");
            int id = rs.getInt("Id");
            String image = rs.getString("Image");
            int lead = rs.getInt("Lead");
            String enabledisplay = rs.getString("EnableDisplay");
            Product product = new Product();
            product.setEnableDisplay(enabledisplay);
            product.setDescription(description);
            product.setSize(size);
            product.setPrice(price);
            product.setPer(per);
            product.setImage(image);
            product.setId(id);
            product.setLeadTime(lead);
            product.setSessionId(sessionId);

            list.add(product);
        }
        return list;
    }

    public static List<Product> getLatestProducts(Connection conn) throws SQLException {
        String sql = "Select Id,Description, Size, Price, Image from Products where EnableDisplay = 'Y' order by DtAdded desc LIMIT 4";

        PreparedStatement pstm = conn.prepareStatement(sql);

        ResultSet rs = pstm.executeQuery();
        List<Product> list = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("Id");
            String description = rs.getString("Description");
            String size = rs.getString("Size");
            BigDecimal price = rs.getBigDecimal("Price");
            String image = rs.getString("Image");
            Product product = new Product();
            product.setId(id);
            product.setDescription(description);
            product.setSize(size);
            product.setPrice(price);
            product.setImage(image);

            list.add(product);
        }
        return list;
    }

    public static void addToCart(Connection conn, Product product) throws SQLException {
        String sql = "Insert into GeneralPolybagOrderpad(sessionid, Size, CreateDatetime, Description, Material, Note, Qty, Cost, JSON, LNo,Per,Leadtime, productImage) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, product.getSessionId());
        pstm.setString(2, product.getSize());
        pstm.setObject(3, LocalDateTime.now());
        pstm.setString(4, product.getDescription());
        pstm.setInt(5, product.getId());
        pstm.setString(6, product.getDescription());
        pstm.setInt(7, product.getQty());
        pstm.setBigDecimal(8, product.getPrice());
        pstm.setString(9, product.getJSON());
        pstm.setInt(10, product.getLNo());
        pstm.setInt(11, product.getPer());
        pstm.setInt(12, product.getLeadTime());
        pstm.setString(13, product.getImage());

        pstm.executeUpdate();
    }

    public static void removeFromCart(Connection conn, Product product) throws SQLException {
        String sql = "Delete From GeneralPolybagOrderpad where JSON = ? and sessionId = ? and sent is NULL and basketId is NULL";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, product.getJSON());
        pstm.setString(2, product.getSessionId());
        pstm.executeUpdate();
    }

    public static boolean updateCart(Connection conn, Product product) throws SQLException {
        String sql = "Update GeneralPolybagOrderpad set JSON = ?, Qty = ? where sessionId = ? and LNo = ?";
        Boolean updated = false;

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, product.getJSON());
        pstm.setInt(2, product.getQty());
        pstm.setString(3, product.getSessionId());
        pstm.setInt(4, product.getLNo());

        int n = pstm.executeUpdate();

        if (n == 1) {
            updated = true;
        }

        return updated;
    }

    public static int generateBasketIdNumber(Connection conn) throws SQLException {
        String sql = "Select Count(*) as basketId from GeneralPolybagOrderpad";
        int basketIdInt = 0;
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            basketIdInt = rs.getInt("basketId");
        }

        return basketIdInt;
    }

    public static String generatePurchaseOrderIdNumber(Connection conn) throws SQLException {
        String sql = "Select Count(*) as number from so_import_raw";
        int POIdNumber = 0;
        String POId = "452000";
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            POIdNumber = rs.getInt("number");
        }
        POId += POIdNumber;
        return POId;
    }

    public static HashMap<String, Discount> getDiscounts(Connection conn) throws SQLException {
        String sql = "Select id, Discount, Per, Enabled from Discounts order by Discount asc";

        PreparedStatement pstm = conn.prepareStatement(sql);

        ResultSet rs = pstm.executeQuery();
        HashMap<String, Discount> discounts = new HashMap<>();
        int loopcount = 0;
        while (rs.next()) {
            int discountPercentage = rs.getInt("Discount");
            int per = rs.getInt("Per");
            int id = rs.getInt("Id");
            String enabled = rs.getString("Enabled");
            Discount discount = new Discount(discountPercentage, per, id, enabled);
            if (loopcount == 0) {
                discounts.put("lowerDiscount", discount);
                loopcount++;
            } else {
                discounts.put("upperDiscount", discount);
            }
        }
        return discounts;
    }

    /*public static Discount getDiscount(Connection conn, Product product) throws SQLException {
        String sql = "Select Id, Discount, Per from Discounts where productID = ?";
        Discount discount = new Discount();
        int count = 1;
        int discountLow = 0;
        int discountHigh = 0;
        int low = 0;
        int high = 0;
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setInt(1, product.getId());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            int per = rs.getInt("Per");
            int discountPercentage = rs.getInt("Discount");
            if (count == 1) {
                low = per;
                discountLow = discountPercentage;
            }
            if (count == 2) {
                high = per;
                discountHigh = discountPercentage;
            }
            count++;
        }

        if (product.getQty() < low) {
            //discount.setDiscount(0);
        }
        if (product.getQty() == low) {
            discount.setDiscount(discountLow);
        }
        if (product.getQty() > low && product.getQty() < high) {
            discount.setDiscount(discountLow);
        }
        if (product.getQty() >= high) {
            discount.setDiscount(discountHigh);
        }

        return discount;
    }*/
    public static void confirmItemsGPO(Connection conn, String basketId, String sessionId) throws SQLException {
        String sql = "Update GeneralPolybagOrderpad set sent = ?, basketId = ? where sessionId = ? and basketId is null and sent is null";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setObject(1, LocalDateTime.now());
        pstm.setString(2, basketId);
        pstm.setString(3, sessionId);

        pstm.executeUpdate();
    }

    public static List<Product> getCart(Connection conn, String sessionId) throws SQLException {
        String sql = "Select Material, Cost, Qty, LNo, JSON, Size, Per, Leadtime, productImage from GeneralPolybagOrderpad where sessionid = ? and sent is null";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, sessionId);

        ResultSet rs = pstm.executeQuery();

        List<Product> products = new ArrayList<Product>();
        while (rs.next()) {

            int materialid = rs.getInt("Material");
            BigDecimal cost = rs.getBigDecimal("Cost");
            int lineNo = rs.getInt("LNo");
            int qty = rs.getInt("Qty");
            String size = rs.getString("Size");
            int per = rs.getInt("Per");
            String JSON = rs.getString("JSON");
            int leadtime = rs.getInt("leadtime");
            String image = rs.getString("productImage");

            Product product = new Product();
            product.setId(materialid);
            product.setPrice(cost);
            product.setSize(size);
            product.setPer(per);
            product.setQty(qty);
            product.setLNo(lineNo);
            product.setJSON(JSON);
            product.setLeadTime(leadtime);
            product.setImage(image);
            products.add(product);
        }
        return products;
    }

    public static List<Product> getCartCheck(Connection conn, String sessionId) throws SQLException {
        String sql = "Select JSON from GeneralPolybagOrderpad where sessionid = ? and sent is null";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, sessionId);

        ResultSet rs = pstm.executeQuery();

        List<Product> products = new ArrayList<Product>();
        while (rs.next()) {

            String JSON = rs.getString("JSON");

            Product product = new Product();

            product.setJSON(JSON);
            products.add(product);

        }
        return products;
    }

    public static List<Product> getCartForPaymentCheck(Connection conn, String basketId) throws SQLException {
        String sql = "Select Material, Cost, Qty, LNo, JSON, Size, Per, Leadtime, productImage from GeneralPolybagOrderpad where basketId = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, basketId);

        ResultSet rs = pstm.executeQuery();

        List<Product> products = new ArrayList<Product>();
        while (rs.next()) {

            int materialid = rs.getInt("Material");
            BigDecimal cost = rs.getBigDecimal("Cost");
            int lineNo = rs.getInt("LNo");
            int qty = rs.getInt("Qty");
            String size = rs.getString("Size");
            int per = rs.getInt("Per");
            String JSON = rs.getString("JSON");
            int leadtime = rs.getInt("leadtime");
            String image = rs.getString("productImage");

            Product product = new Product();
            product.setId(materialid);
            product.setPrice(cost);
            product.setSize(size);
            product.setPer(per);
            product.setQty(qty);
            product.setLNo(lineNo);
            product.setJSON(JSON);
            product.setLeadTime(leadtime);
            product.setImage(image);
            products.add(product);
        }
        return products;
    }

    public static void placeOrder(Connection conn, Order order) throws SQLException {
        String sql = "Insert into SO_Import_Raw (basketid, CustomerName, Email, shipToStreet1, shipToStreet2, shipToStreet3,County, shipToPostcode, Country, Telephone, total, subTotal, VAT, Currency, OrderSent,DueDate, PromoCode, Notes, Usercode, Leadtime, JSON,POId,delivery_price,Loaded) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, order.getBasketId());
        pstm.setString(2, order.getCustomerName());
        pstm.setObject(3, order.getEmail());
        pstm.setString(4, order.getShipToStreet1());
        pstm.setString(5, order.getShipToStreet2());
        pstm.setString(6, order.getShipToStreet3());
        pstm.setString(7, order.getCounty());
        pstm.setString(8, order.getShipToPostcode());
        pstm.setString(9, order.getCountry());
        pstm.setString(10, order.getTelephone());
        pstm.setBigDecimal(11, order.getTotal());
        pstm.setBigDecimal(12, order.getSubtotal());
        pstm.setInt(13, order.getVAT());
        pstm.setString(14, order.getCurrency());
        pstm.setObject(15, order.getOrderSent());
        pstm.setObject(16, order.getDueDate());
        if (order.getPromocode() != null) {
            pstm.setString(17, order.getPromocode().getDescription());
        } else {
            pstm.setString(17, null);
        }
        pstm.setString(18, order.getNotes());
        pstm.setString(19, order.getUsercode());
        pstm.setInt(20, order.getLeadTime());
        pstm.setString(21, order.getJSON());
        pstm.setString(22, order.getPOId());
        pstm.setBigDecimal(23, order.getDeliveryPrice());
        pstm.setString(24, "X");
        insertBillingAddress(conn, order);
        pstm.executeUpdate();

    }

    public static Boolean checkOrderLoaded(Connection conn, String POId) throws SQLException {
        String sql = "Select Loaded from SO_Import_Raw where POId = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, POId);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            String loaded = rs.getString("Loaded");
            if (loaded.contains("X")) {
                return true;
            }
        }
        return false;
    }

    public static int queryLeadTime(Connection conn, Product product) throws SQLException {
        String sql = "Select Lead from Products where Id = ?";
        int leadtime = 0;
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setInt(1, product.getId());

        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            leadtime = rs.getInt("Lead");
        }

        return leadtime;
    }

    public static Order queryOrder(Connection conn, String basketId) throws SQLException {
        String sql = "Select Id, basketid, POId, CustomerName, Email, shipToStreet1, shipToStreet2, shipToStreet3, County, shipToPostcode, Country, Telephone,DueDate, OrderSent, PromoCode, Notes, Leadtime, JSON, delivery_price, Loaded from So_import_raw where basketid = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, basketId);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            int Id = rs.getInt("Id");
            String POId = rs.getString("POId");
            String CustomerName = rs.getString("CustomerName");
            String Email = rs.getString("Email");
            String shipToStreet1 = rs.getString("shipToStreet1");
            String shipToStreet2 = rs.getString("shipToStreet2");
            String shipToStreet3 = rs.getString("shipToStreet3");
            String County = rs.getString("County");
            String shipToPostcode = rs.getString("shipToPostCode");
            //Telephone, total, subTotal, DueDate, OrderSent, PromoCode, Notes, Leadtime, JSON, delivery_price, Loaded
            String Country = rs.getString("Country");
            String Telephone = rs.getString("Telephone");
            LocalDate DueDate = rs.getObject("DueDate", LocalDate.class);
            LocalDateTime OrderSent = rs.getObject("OrderSent", LocalDateTime.class);
            String PromoCode = rs.getString("PromoCode");
            String Notes = rs.getString("Notes");
            BigDecimal delivery_price = rs.getBigDecimal("delivery_price");
            String Loaded = rs.getString("Loaded");
            int Leadtime = rs.getInt("Leadtime");
            String JSON = rs.getString("JSON");

            Order order = new Order(Id, basketId, POId, CustomerName, Email, shipToStreet1, shipToStreet2, shipToStreet3, County, shipToPostcode, Country, Telephone, DueDate, OrderSent, PromoCode, Notes, Leadtime, JSON, delivery_price, Loaded);

            order.setJSON(JSON);
            order.setLeadTime(Leadtime);
            return order;
        }

        return null;
    }
    
    public static void resetOrder(Connection conn, String basketId, String POId) throws SQLException{
        String sql = "Update GeneralPolybagOrderpad set sent = null, basketId = null where basketId = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setObject(1, basketId);       
        pstm.executeUpdate();
        
        String sql2 = "Delete from So_import_raw where POId = ?";
        PreparedStatement pstmTwo = conn.prepareStatement(sql2);
        
        pstmTwo.setString(1, POId);
        pstmTwo.executeUpdate();

    }

    public static int getMaxLeadTime(Connection conn) throws SQLException {
        String sql = "Select Max(Lead) as Lead from Products";
        int leadtime = 0;
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            leadtime = rs.getInt("Lead");
        }
        return leadtime;
    }

    public static List<LeadTime> getLeadTimes(Connection conn, int lead) throws SQLException {
        String sql = "Select Description, Price, AutomaticSelection,info, leadtime from LeadTimes where leadtime <= ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setInt(1, lead);

        ResultSet rs = pstm.executeQuery();

        List<LeadTime> list = new ArrayList<>();
        while (rs.next()) {
            String description = rs.getString("Description");
            String info = rs.getString("info");
            BigDecimal price = rs.getBigDecimal("Price");
            BigDecimal automaticSelection = rs.getBigDecimal("AutomaticSelection");
            LeadTime leadtime = new LeadTime(description, rs.getInt("leadtime"), price, automaticSelection, info);
            list.add(leadtime);
        }
        return list;
    }

    public static LeadTime getLeadTime(Connection conn, int lead) throws SQLException {
        String sql = "Select Description, Price, AutomaticSelection,info, leadtime from LeadTimes where leadtime = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setInt(1, lead);

        ResultSet rs = pstm.executeQuery();
        LeadTime leadtime = new LeadTime();

        String description = "", info = "";
        BigDecimal price = new BigDecimal(0), automaticSelection = new BigDecimal(0);
        while (rs.next()) {
            description = rs.getString("Description");
            System.out.println(lead);
            System.out.println(description);
            info = rs.getString("info");
            price = rs.getBigDecimal("Price");
            automaticSelection = rs.getBigDecimal("AutomaticSelection");
            leadtime = new LeadTime(description, rs.getInt("leadtime"), price, automaticSelection, info);
        }
        return leadtime;
    }

    public static String logAudit(Connection conn, AuditHandler audit) throws SQLException {
        String sql = "INSERT INTO PolybagsHelpers_audit(Action, DtTm, Page, JSON, SessionId) values(?,?,?,?,?)";

        PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstm.setString(1, audit.getAction());
        pstm.setObject(2, LocalDateTime.now());
        pstm.setString(3, audit.getPage());
        pstm.setString(4, audit.getJSON());
        pstm.setString(5, audit.getSessionId());
        pstm.executeUpdate();
        ResultSet rs = pstm.getGeneratedKeys();
        String id = "";
        if (rs.next()) {
            id = rs.getString(1);
            System.out.println(id);
        }
        return id;
    }

    public static void insertBillingAddress(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO Billing_Addresses(basketId, CustomerName, firstLineAddress, secondLineAddress, thirdLineAddress, City, County, PostalCode) values(?,?,?,?,?,?,?,?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, order.getBasketId());
        pstm.setObject(2, order.getCustomerName());
        pstm.setString(3, order.getFirstLineBillingAddress());
        pstm.setString(4, order.getBillingSecondLineAddress());
        pstm.setString(5, order.getBillingThirdLineAddress());
        pstm.setString(6, order.getBillingAddressCity());
        pstm.setString(7, order.getBillingAddressCounty());
        pstm.setString(8, order.getBillingAddressPostalCode());

        pstm.executeUpdate();

    }

    public static PromoCodes getPromoCode(Connection conn, String promocode) throws SQLException {
        String sql = "Select * from PromoCodes where PromoCode = ?";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, promocode);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            BigDecimal value = rs.getBigDecimal("Discount");
            PromoCodes promoCode = new PromoCodes(promocode, value);
            return promoCode;
        }
        return null;
    }

    public static void intermediaryPromocode(Connection conn, String promocode, String sessionId) throws SQLException {
        String sql = "insert into applyPromocode(sessionId, promocode)Values(?,?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, sessionId);
        pstm.setString(2, promocode);
        pstm.executeUpdate();

    }

    public static PromoCodes getIntermediaryPromocode(Connection conn, String sessionId) throws SQLException {
        String sql = "Select PromoCode, Discount from vw_PromocodesApplied where sessionId = ?";
        PreparedStatement pstm = conn.prepareStatement(sql);

        pstm.setString(1, sessionId);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            String description = rs.getString("PromoCode");
            System.out.println(description);
            BigDecimal discount = rs.getBigDecimal("Discount");
            PromoCodes promocode = new PromoCodes(description, discount);
            return promocode;
        }

        return null;
    }

    public static void insertNewsletterEmailAddress(Connection conn, String emailaddress) throws SQLException {
        String sql = "Insert into newsletterEmailAddress(emailaddress)VALUES(?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, emailaddress);

        pstm.executeUpdate();
    }
}
