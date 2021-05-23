/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.beans;

import com.google.gson.Gson;
import com.polybags.utils.DBUtils;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author bencleary
 */
public class Order {

    private String customerName, firstName, lastName, email, shipToStreet1, shipToStreet2, shipToStreet3 = null,
            shipToPostcode, county, country, telephone = null, currency, notes = null,
            usercode = null, basketId, firstLineBillingAddress, billingSecondLineAddress,
            billingThirdLineAddress, billingAddressCity, billingAddressCounty, billingAddressPostalCode, billingAddressCountry;
    private BigDecimal subtotal = new BigDecimal(0), total = new BigDecimal(0), deliveryPrice = new BigDecimal(0);
    private int id, VAT = 0, leadtime;
    private LocalDateTime orderSent;
    private LocalDate dueDate;
    private String productsJSON, orderJSON, POId;
    private List<Product> products;
    private PromoCodes promocode = null;

    public Order() {

    }

    public Order(String customerName, String email, String shipToStreet1, String shipToStreet2, String shipToStreet3, String shipToPostcode, String country, String telephone) {
        this.customerName = customerName;
        this.email = email;
        this.shipToStreet1 = shipToStreet1;
        this.shipToStreet2 = shipToStreet2;
        this.shipToStreet3 = shipToStreet3;
        this.shipToPostcode = shipToPostcode;
        this.country = country;
        this.telephone = telephone;

    }

    public Order(int id, String basketId, String POId, String customerName, String email, String shipToStreet1, String shipToStreet2, String shipToStreet3, String county, String shipToPostcode, String country, String Telephone, LocalDate dueDate, LocalDateTime orderSent, String PromoCode, String Notes, int leadtime, String JSON, BigDecimal delivery_price, String Loaded) {
        this.customerName = customerName;
        this.email = email;
        this.shipToStreet1 = shipToStreet1;
        this.shipToStreet2 = shipToStreet2;
        this.shipToPostcode = shipToPostcode;
        this.county = county;
        this.country = country;
        this.basketId = basketId;
        this.id = id;
        this.leadtime = leadtime;
        this.orderSent = orderSent;
        this.dueDate = dueDate;
        this.orderJSON = JSON;
        this.POId = POId;
    }

    public String getPOId() {
        return this.POId;
    }

    public int getId() {
        return this.id;
    }

    public String getBasketId() {
        return this.basketId;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getShipToStreet1() {
        return this.shipToStreet1;
    }

    public String getShipToStreet2() {
        if (this.shipToStreet2 == null) {
            this.shipToStreet2 = "";
        }
        return this.shipToStreet2;
    }

    public String getShipToStreet3() {
        if (this.shipToStreet3 == null) {
            this.shipToStreet3 = "";
        }
        return this.shipToStreet3;
    }

    public String getShipToPostcode() {
        return this.shipToPostcode;
    }

    public String getCounty() {
        return this.county;
    }

    public String getCountry() {
        return this.country;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNotes() {
        return this.notes;
    }

    public String getUsercode() {
        return this.usercode;
    }

    public int getLeadTime() {
        return this.leadtime;
    }

    public int getVAT() {
        return this.VAT;
    }

    public BigDecimal getDeliveryPrice() {
        return this.deliveryPrice;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public LocalDateTime getOrderSent() {
        return this.orderSent = LocalDateTime.now();
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public String getProductsJSON() {
        return this.productsJSON;
    }

    public String getJSON() {
        return this.orderJSON;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerName() {
        this.customerName = this.firstName + " " + this.lastName;

    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setShipToStreet1(String street1) {
        this.shipToStreet1 = street1;
    }

    public void setShipToStreet2(String street2) {
        this.shipToStreet2 = street2;
    }

    public void setShipToStreet3(String street3) {
        this.shipToStreet3 = street3;
    }

    public void setShipToPostcode(String postcode) {
        this.shipToPostcode = postcode;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setJSON(String JSON) {
        if (this.getProductsJSON() != null) {
            this.orderJSON = JSON + this.getProductsJSON();
        }
        if (this.getProductsJSON() == null) {
            this.orderJSON = JSON;
        }
    }

    public void setPOId(String POId) {
        this.POId = POId;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public void setLeadTime(int leadtime) {
        this.leadtime = leadtime;
    }

    public void setVAT(int VAT) {
        this.VAT = VAT;
    }

    public void setSubtotal() {
        for (Product product : products) {
            this.subtotal = subtotal.add(product.calculateSubtotal());
        }

    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setTotal(Connection conn) {
        try {
            HashMap<String, Discount> discounts = DBUtils.getDiscounts(conn);
            int qty = 0;
            for (Product product : products) {
                this.total = this.total.add(product.calculateTotal(conn));
                qty += product.getQty();
            }
            this.total = minusDiscounts(discounts, qty);
            if (this.promocode != null) {
                this.total = this.total.multiply(this.promocode.getPromoValue());
            }
            this.total = this.total.add(this.deliveryPrice);
            this.total = this.total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } catch (Exception e) {
        }
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public void setPromocode(PromoCodes promocode) {
        this.promocode = promocode;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setProductsJSON() {
        for (Product product : this.products) {
            this.productsJSON += product.getJSON();
        }
    }

    public void setOrderSent(LocalDateTime orderSent) {
        this.orderSent = orderSent;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public void setBillingAddressCountry(String billingAddressCountry) {
        this.billingAddressCountry = billingAddressCountry;
    }

    public String getBillingAddressCountry() {
        return billingAddressCountry;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setFirstLineBillingAddress(String firstLineBillingAddress) {
        this.firstLineBillingAddress = firstLineBillingAddress;
    }

    public void setBillingSecondLineAddress(String billingSecondLineAddress) {
        this.billingSecondLineAddress = billingSecondLineAddress;
    }

    public void setBillingThirdLineAddress(String billingThirdLineAddress) {
        this.billingThirdLineAddress = billingThirdLineAddress;
    }

    public void setBillingAddressCity(String billingAddressCity) {
        this.billingAddressCity = billingAddressCity;
    }

    public void setBillingAddressCounty(String billingAddressCounty) {
        this.billingAddressCounty = billingAddressCounty;
    }

    public void setBillingAddressPostalCode(String billingAddressPostalCode) {
        this.billingAddressPostalCode = billingAddressPostalCode;
    }

    public void setLeadtime(int leadtime) {
        this.leadtime = leadtime;
    }

    public void setProductsJSON(String productsJSON) {
        this.productsJSON = productsJSON;
    }

    public void setOrderJSON(String orderJSON) {
        this.orderJSON = orderJSON;
    }

    public PromoCodes getPromocode() {
        return promocode;
    }

    public String getFirstLineBillingAddress() {
        return firstLineBillingAddress;
    }

    public String getBillingSecondLineAddress() {
        return billingSecondLineAddress;
    }

    public String getBillingThirdLineAddress() {
        return billingThirdLineAddress;
    }

    public String getBillingAddressCity() {
        return billingAddressCity;
    }

    public String getBillingAddressCounty() {
        return billingAddressCounty;
    }

    public String getBillingAddressPostalCode() {
        return billingAddressPostalCode;
    }

    public int getLeadtime() {
        return leadtime;
    }

    public String getOrderJSON() {
        return orderJSON;
    }

    public BigDecimal convertToBigDecimal(float Integer) {
        BigDecimal number = new BigDecimal(Integer);
        return number;
    }

    public BigDecimal minusDiscounts(HashMap<String, Discount> discounts, int qty) {
        Discount lowerDiscount = discounts.get("lowerDiscount");
        Discount upperDiscount = discounts.get("upperDiscount");

        if (qty >= lowerDiscount.getPer() && qty < upperDiscount.getPer()) {
            return this.total = this.total.subtract(this.total.multiply(convertToBigDecimal(lowerDiscount.getDiscount()).divide(convertToBigDecimal(100))));
        }
        if (qty >= upperDiscount.getPer()) {
            return this.total = this.total.subtract(this.total.multiply(convertToBigDecimal(upperDiscount.getDiscount()).divide(convertToBigDecimal(100))));
        }
        if (qty < lowerDiscount.getPer()) {
            return this.total;
        }

        return null;
    }

}
