package com.acme;

public abstract class Users {
    String userName;
    String password;
    String firstName;
    String type;

    Users(String userName, String password, String firstName,String type) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.type = type;
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

    public String getType() {
        return type;
    }

    @Override
    public String toString(){
        return type + "," + userName + "," + password + "," + firstName;

    }

    public String[] getUser(Users user){
        return user.toString().split(",");
    }

    public boolean verifyPassword(String password){
        // to be changed to hashing
        return this.password.equals(password);
    }
}
