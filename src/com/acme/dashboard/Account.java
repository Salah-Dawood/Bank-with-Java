package com.acme.dashboard;

import com.acme.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

abstract class Account {
    protected int accID;
    protected Users user;
    protected String accountType;
    protected double balance;
    protected double overDraftTotal;
    protected boolean isActive;
    protected Mastercard card;

    public Account(String accountType) throws IOException {
        this.user = Session.getLoggedInUser();
        this.accID = generateAccountID();
        this.accountType = accountType;
        this.overDraftTotal = 0;
        this.isActive = true;
        this.balance = 0;
        this.card = new Mastercard();
    }

    public String toString(){
        return this.user.getUserName() + "|" + String.valueOf(accID) + "|" + accountType + "|" + String.valueOf(overDraftTotal) + "|" + String.valueOf(balance) + "|" + String.valueOf(isActive) + "|" + String.valueOf(card.getCardID());

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
}


