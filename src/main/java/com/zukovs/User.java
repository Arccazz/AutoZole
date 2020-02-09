package com.zukovs;

public class User {
    private String mail;
    private String password;
    private int balance;
    private String userID;

    public User(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}
