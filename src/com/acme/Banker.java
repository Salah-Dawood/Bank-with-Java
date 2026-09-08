package com.acme;

public class Banker extends Users {

    public Banker(String userName, String password, String firstName) {
        super(userName, password, firstName);
    }

    protected void verifyName(String userName){
        if (userName.equals(this.userName)){
            System.out.println("logged in");
        }
        System.out.println(this.userName);
    }


}
