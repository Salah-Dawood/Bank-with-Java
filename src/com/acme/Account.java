package com.acme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class Account {
    protected int accID;
    protected Users user;
    protected double balance;
    protected double overDraftTotal;
    protected boolean isActive;
    protected Mastercard card;

    public Account() throws IOException {
        this.user = Session.getLoggedInUser();
        this.accID = generateAccountID();
        this.overDraftTotal = 0;
        this.isActive = true;
        this.balance = 0;
        this.card = new Mastercard();
    }

    public String toString(){
        return String.valueOf(accID) + "|" + getClass().getSimpleName() + "|" + String.valueOf(balance) + "|" + String.valueOf(overDraftTotal) + "|" + String.valueOf(isActive) + "|" + String.valueOf(card.getClass().getSimpleName());
    }

    public String newAccountInsertion(){
        System.out.println(FileService.getUserAccountsInfo(user));

        return "";
    }

    private int generateAccountID() throws IOException {
        Set<String> existingIds = Files.lines(FileDBConfig.usersFile)
                .map(line -> line.split(",", 5))
                .filter(parts -> parts.length == 5 && !parts[4].isEmpty())
                .flatMap(parts -> Arrays.stream(parts[4].split(";")))
                .map(acc -> acc.split("\\|", -1))
                .filter(fields -> fields.length == 7)
                .map(fields -> fields[1])
                .collect(Collectors.toSet());

        Random rand = new Random();
        String id;
        do {
            id = String.valueOf(1000 + rand.nextInt(9000));
        } while (existingIds.contains(id));

        return Integer.valueOf(id);
    }

    public int getAccID() {
        return accID;
    }
    public void setAccID(int accID) {
        this.accID = accID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) throws IOException {
        this.balance = balance;
        FileService.updateUserLine(user);
    }

    public double getOverDraftTotal() {
        return overDraftTotal;
    }

    public void setOverDraftTotal(double overDraftTotal) {
        this.overDraftTotal = overDraftTotal;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Mastercard getCard() {
        return card;
    }

    public void setCard(Mastercard card) {
        this.card = card;
    }
}


