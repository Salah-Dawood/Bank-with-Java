package com.acme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Users {
    String userName;
    String password;
    String firstName;
    String type;
    List<Account> accounts;

    Users(String userName, String password, String firstName,String type) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.accounts = new ArrayList<>();
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

    protected List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public String toString(){
        System.out.println(accounts);
        return getClass().getSimpleName() + "," + userName + "," + password + "," + firstName + "," + getAccountsData();
    }

    public String[] getUser(Users user){
        return user.toString().split(",");
    }

    public boolean verifyPassword(String password){
        // to be changed to hashing
        return this.password.equals(password);
    }


    public void createChecking() throws IOException {
        this.accounts.add(new CheckingAccount());
        FileService.updateUserLine(this);
    }

    public void createSavings() throws IOException {
        this.accounts.add(new SavingsAccount());
        FileService.updateUserLine(this);
    }
    public String getAccountsData() {
        String accountsData;
        if (accounts == null || accounts.isEmpty()) {
            accountsData = "No Accounts";
        } else {
            // Joins accounts nicely. Example: "Savings,Checking" instead of "[Savings, Checking]"
            accountsData = accounts.stream()
                    .map(Account::toString)
                    .collect(java.util.stream.Collectors.joining(";")); // Using ';' prevents breaking your primary commas
        }
        return accountsData;
    }

}
