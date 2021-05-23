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
public class Email {
    private String email, firstname,lastname,subject,message,webpage;

    public Email() {
    }

    public Email(String email, String firstname, String lastname, String subject, String message, String webpage) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.subject = subject;
        this.message = message;
        this.webpage = webpage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    
    
    
    
}
