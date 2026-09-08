package com.acme;

public abstract class Users {
    String userName;
    String password;
    String firstName;

    Users(String userName, String password, String firstName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString(){
        return "Username " + userName;
    }

    public boolean verifyPassword(String password){
        // to be changed to hashing
        return this.password.equals(password);
    }
}
