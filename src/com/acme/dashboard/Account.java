package com.acme.dashboard;

abstract class Account {
    protected int accID;
    protected BankUsers user;
    protected String accountType;
    protected double balance;
    protected double overDraftTotal;
    protected boolean hasCard;
    protected boolean isActive;

    public Account(BankUsers user, int accID, String accountType) {
        this.user = user;
        this.accID = accID;
        this.overDraftTotal = 0;
        this.hasCard = false;
        this.isActive = true;
        this.balance = 0;
        this.accountType = accountType;
    }
}


