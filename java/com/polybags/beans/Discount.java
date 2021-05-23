/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.beans;

/**
 *
 * @author bencleary
 */
public class Discount {

    private int discount, per, id;
    //int productId;
    private String enabled;
    public Discount() {

    }

//    public Discount(int discount, int productId, int per, int id){
//        this.discount = discount;
//        this.productId = productId;
//        this.per = per;
//    }
    public Discount(int discount, int per, int id, String enabled) {
        this.discount = discount;
        this.per = per;
        this.id = id;
        this.enabled = enabled;
    }

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public int getId() {
        return this.id;
    }

    public int getDiscount() {
        return this.discount;
    }

//    public int getProductId() {
//        return this.productId;
//    }

    public int getPerDiscount() {
        return this.per;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

//    public void setProductId(int productId) {
//        this.productId = productId;
//    }

    public void setPerDiscount(int per) {
        this.per = per;
    }

    public void setId(int id) {
        this.id = id;
    }
}
