/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.polybags.beans;

import java.math.BigDecimal;

/**
 *
 * @author bencleary
 */
public class PromoCodes extends Discount {
    private String description;
    private BigDecimal promoValue = new BigDecimal(0);
    private Boolean promocodeSet = false;

    public PromoCodes(){
    
    }
    
    public PromoCodes(String promoCode, BigDecimal promoValue) {
        this.description = promoCode;
        this.promoValue = promoValue;
        this.promocodeSet = true;
    }
    
    public void setPromocodeSet(Boolean promocodeSet) {
        this.promocodeSet = promocodeSet;
    }
    
    public Boolean getPromocodeSet() {
        return promocodeSet;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setPromoValue(BigDecimal promoValue) {
        this.promoValue = promoValue;
    }

    public BigDecimal getPromoValue() {
        return promoValue;
    }

    public String getDescription() {
        return description;
    }
    
    public BigDecimal applyPromocode(BigDecimal total){
        total = total.multiply(this.promoValue);        
        return total;
    }


}
