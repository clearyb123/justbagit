/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.beans;

import com.polybags.utils.DBUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;

/**
 *
 * @author bencleary
 */
public class Product {
    private String description, image, image2,image3,image4,size,sessionId, JSON, usercode, enableDisplay;

    
    private BigDecimal price,subtotal, total;
    private int per,id, qty, LNo, leadTime;
    public static final BigDecimal percent = new BigDecimal(100);

    public Product(){
     
    }
    
    public Product(String description,String image, BigDecimal price, String size, int per){
        this.description = description;
        this.image = image;
        this.price = price;
        this.size = size;
        this.per = per;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
    
    public String getDescription(){
       return this.description;
    }
    
    public String getUserCode(){
        return this.usercode;
    }
    
    public String getJSON(){
        return this.JSON;
    }
    
    public int getLNo(){
        return this.LNo;
    }
    
    public int getQty(){
        return this.qty;
    }
    
    public String getSessionId(){
        return this.sessionId;
    }
    
    public String getImage(){
       return this.image;
    }
    public String getSize(){
       return this.size;
    }
    
    public int getPer(){
       return this.per;
    }
    
    public int getLeadTime(){
        return this.leadTime;
    }
    
    public int getId(){
       return this.id;
    }
    
    public BigDecimal getPrice(){
       return this.price;
    }
    

    
    
    public void setDescription(String description){
       this.description = description;
    }
    
    public void setImage(String image){
       this.image = image;
    }
    public void setSize(String size){
       this.size = size;
    }
    
    public void setPer(int per){
      this.per = per;
    }
    
    public void setPrice(BigDecimal price){
       this.price = price;
    }
    
    public void setId(int id){
      this.id = id;
    }
    
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
    
    public void setQty(int qty){
        this.qty = qty;
    }
    
    public void setLNo(int LNo){
        this.LNo = LNo;
    }
    
    public void setJSON(String JSON){
        this.JSON = JSON;
    }
    
    public void setLeadTime(int leadtime){
        this.leadTime = leadtime;
    }
    
    public void setUserCode(String usercode){
        this.usercode = usercode;
    }
    
    public String getEnableDisplay() {
        return enableDisplay;
    }

    public void setEnableDisplay(String enableDisplay) {
        this.enableDisplay = enableDisplay;
    }
    
    public BigDecimal convertToBigDecimal(int Integer){
        BigDecimal number = new BigDecimal(Integer);
        return number;
    }
    
    public BigDecimal calculateSubtotal(){
        this.subtotal = this.getPrice().multiply(this.convertToBigDecimal(this.getQty()));

        return this.subtotal.setScale(2,RoundingMode.HALF_UP);
    }
    
    public BigDecimal calculateTotal(Connection conn){
        try{
            //Discount discountData = DBUtils.getDiscount(conn, this);
           // BigDecimal discount = new BigDecimal(discountData.getDiscount());
            //this.total = this.calculateSubtotal().multiply(discount.divide(percent));
            if(this.subtotal == null){
                this.total = this.calculateSubtotal();
            }else{
                this.total = this.subtotal;
            }
            //this.total = this.subtotal.subtract(this.total);
        }catch(Exception e){
            e.printStackTrace();
        }
        this.total  = this.total.setScale(2,RoundingMode.HALF_UP);
        return this.total;
    }
    
}
