package com.lizxing.securitydemo.pojo;

import java.io.Serializable;

public class MyUser implements Serializable {

    private String userName;
    private String password;
    private String identity;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
