package com.acme.dashboard;

import com.acme.Users;

abstract class Account {
    protected int accID;
    protected Users user;
    protected String accountType;
    protected double balance;
    protected double overDraftTotal;
    protected boolean hasCard;
    protected boolean isActive;

    public Account(Users user, int accID, String accountType) {
        this.user = user;
        this.accID = accID;
        this.overDraftTotal = 0;
        this.hasCard = false;
        this.isActive = true;
        this.balance = 0;
        this.accountType = accountType;
    }
}


