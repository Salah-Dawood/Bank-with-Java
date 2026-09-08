package com.acme.dashboard;

public class Banker extends BankUsers{

    public Banker(String userName, String password, String firstName, String lastName) {
        super(userName, password, firstName, lastName);
    }

    protected void verifyName(String userName){
        if (userName.equals(this.userName)){
            System.out.println("logged in");
        }
        System.out.println(this.userName);
    }


}
