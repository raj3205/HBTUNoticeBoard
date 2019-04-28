package com.example.hbtunoticeboard;

public class UserProfile {

    String name;
    String mobile;
    String srNumber;
    String email;

    public UserProfile(){

    }

    public UserProfile(String name, String mobile, String srNumber, String email) {
        this.name = name;
        this.mobile = mobile;
        this.srNumber = srNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getSrNumber() {
        return srNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setSrNumber(String srNumber) {
        this.srNumber = srNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
