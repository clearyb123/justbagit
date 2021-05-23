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
public class AuditHandler {
    private String JSON, page, action, sessionId;
    
    public AuditHandler(){
    
    }
    
    public AuditHandler(String JSON, String page, String action, String sessionId){
        this.JSON = JSON;
        this.page = page;
        this.action = action;
        this.sessionId = sessionId;
    }
    
    public String getJSON(){
        return this.JSON;
    }
    
    public String getSessionId(){
        return this.sessionId;
    }
    
    public String getPage(){
        return this.page;
    }
    
    public String getAction(){
        return this.action;
    }
    
    public void setJSON(String json){
        this.JSON = json;
    }
    
    public void setPage(String page){
        this.page = page;
    }
    
    public void setAction(String action){
        this.action = action;
    }
    
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
}
