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
public class LeadTime {
    private String description, info;
    private int leadtime;
    private BigDecimal price, autoselect;
    
    public LeadTime(){
    
    }
    
    public LeadTime(String description, int leadtime, BigDecimal price, BigDecimal autoselect, String info){
        this.description = description;
        this.leadtime = leadtime;
        this.price = price;
        this.autoselect = autoselect;
        this.info = info;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public int getLeadTime(){
        return this.leadtime;
    }
    
    public String getInfo(){
        return this.info;
    }
    
    public BigDecimal getPrice(){
        return this.price;
    }
    
    public BigDecimal getAutoSelect(){
        return this.autoselect;
    }
    
}
