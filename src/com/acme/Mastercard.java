package com.acme;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Mastercard implements IMastercard{
    protected double withdrawLimit;
    protected double transferLimit;
    protected double transferLimitOW;
    protected double depositLimit;
    protected double depositLimitOW;

    public Mastercard() throws IOException {
        this.depositLimitOW = 200_000;
        this.depositLimit = 100_000;
        this.transferLimitOW = 20_000;
        this.transferLimit = 10_000;
        this.withdrawLimit = 5_000;
    }

//    private int generateCardID() throws IOException {
//        Set<String> existingCardIds = Files.lines(FileDBConfig.usersFile)
//                .map(line -> line.split(",", 5))
//                .filter(parts -> parts.length == 5 && !parts[4].isEmpty())
//                .flatMap(parts -> Arrays.stream(parts[4].split(";")))
//                .map(acc -> acc.split("\\|", -1))
//                .filter(fields -> fields.length == 7)
//                .map(fields -> fields[6])
//                .collect(Collectors.toSet());
//
//        Random rand = new Random();
//        String id;
//        do {
//            id = String.valueOf(10000 + (long) (rand.nextDouble() * 90000));
//        } while (existingCardIds.contains(id));
//
//        return Integer.valueOf(id);
//    }

//    public int getCardID() {
//        return cardID;
//    }

    public double getWithdrawLimit() {
        return withdrawLimit;
    }

    public double getTransferLimit() {
        return transferLimit;
    }

    public double getTransferLimitOW() {
        return transferLimitOW;
    }

    public double getDepositLimit() {
        return depositLimit;
    }

    public double getDepositLimitOW() {
        return depositLimitOW;
    }

    public void setWithdrawLimit(double withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    public void setTransferLimit(double transferLimit) {
        this.transferLimit = transferLimit;
    }

    public void setTransferLimitOW(double transferLimitOW) {
        this.transferLimitOW = transferLimitOW;
    }

    public void setDepositLimit(double depositLimit) {
        this.depositLimit = depositLimit;
    }

    public void setDepositLimitOW(double depositLimitOW) {
        this.depositLimitOW = depositLimitOW;
    }
}
