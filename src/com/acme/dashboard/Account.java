package com.acme.dashboard;

import com.acme.Session;
import com.acme.Users;

abstract class Account {
    protected int accID;
    protected Users user;
    protected String accountType;
    protected double balance;
    protected double overDraftTotal;
    protected boolean hasCard;
    protected boolean isActive;

    public Account() {
        this.user = Session.getLoggedInUser();
        this.accID = accID;
        this.overDraftTotal = 0;
        this.isActive = true;
        this.balance = 0;

    }
}


