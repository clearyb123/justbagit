/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bencleary
 */
public class Cart {
    private String sessionId, usercode, JSON, basketId;
    private LocalDateTime CreateDateTime, sent;
    private BigDecimal subTotal, total;
    private List<Product> products = new ArrayList<>();
    
    
    public Cart(){
    
    }
    
    public Cart(BigDecimal total, BigDecimal subTotal, String JSON, LocalDateTime CreateDateTime, List<Product> products){
        this.total = total;
        this.JSON = JSON;
        this.CreateDateTime = CreateDateTime;
        this.products = products;
        this.subTotal = subTotal;
    }
    
    public Cart(String sessionId, String usercode,String JSON, LocalDateTime CreateDateTime,LocalDateTime sent, BigDecimal subTotal,BigDecimal total, List<Product> products){
        this.sessionId =sessionId;
        this.usercode = usercode;
        this.JSON = JSON;
        this.CreateDateTime = CreateDateTime;
        this.sent = sent;
        this.subTotal = total;
        this.products = products;
    }
    
    public String getSessionId(){
        return this.sessionId;
    }
    
    public String getBasketId(){
        return this.basketId;
    }
    
    public String getUsercode(){
        return this.usercode;
    }
    
    public LocalDateTime getCreateDateTime(){
        return this.CreateDateTime;
    }
    
    public BigDecimal getSubTotal(){
        return this.subTotal;
    }
    
    public BigDecimal getTotal(){
        return this.total;
    }
    
    public LocalDateTime getSent(){
        return this.sent;
    }
    
    public List<Product> getProducts(){
        return this.products;
    }
    
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
    
    public void setUsercode(String usercode){
        this.usercode = usercode;
    }
    
    public void setCreateDateTime(LocalDateTime createDateTime){
        this.CreateDateTime = createDateTime;
    }
    
    public void setSubTotal(BigDecimal subTotal){
        this.subTotal = subTotal;
    }
    
    public void setTotal(BigDecimal total){
        this.total = total;
    }
    
    public void setSent(LocalDateTime sent){
        this.sent = sent;
    }
    
    public void setProducts(List<Product> products){
        this.products = products;
    }
    
    public void setBasketId(String basketId){   
        this.basketId = basketId;
    }
}
