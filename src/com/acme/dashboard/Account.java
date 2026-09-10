package com.acme.dashboard;

import com.acme.FileService;
import com.acme.Session;
import com.acme.Users;

import java.io.File;
import java.lang.reflect.Array;

abstract class Account {
    protected int accID;
    protected Users user;
    protected String accountType;
    protected double balance;
    protected double overDraftTotal;
    protected boolean isActive;

    public Account() {
        this.user = Session.getLoggedInUser();
        this.accID = accID;
        this.overDraftTotal = 0;
        this.isActive = true;
        this.balance = 0;

    }

    public String toString(){
        return this.user.getUserName() + "|" + String.valueOf(accID) + "|" + accountType + "|" + String.valueOf(overDraftTotal) + "|" + String.valueOf(balance) + "|" + String.valueOf(isActive);

    }

    public String newAccountInsertion(){
        System.out.println(FileService.getUserAccountsInfo(user));



        return "";
    }
}


