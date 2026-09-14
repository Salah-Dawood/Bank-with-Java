package com.acme;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TransactionsDashboard {
    public static void show() throws IOException {
        Users user = Session.getLoggedInUser();
        boolean exit = false;
        while (!exit) {
            System.out.println("----Transactions----");

            System.out.println("1) New Transaction");
            System.out.println("2) Transactions History");
            System.out.println("E) Exit");

            int option = Tools.enterOption();

            switch (option) {
                case 1:
                    newTransaction();
                    break;
                case 2:

                    break;
                case 100:
                    exit = true;
                    break;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        }
    }

    private static void newTransaction() throws IOException {
        Tools.space(16);
        System.out.println("New Transaction: Choose Account");
        Users user = Session.getLoggedInUser();
        List<Account> accounts = user.getAccounts();

        if (accounts.size() != 0) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ") " + accounts.get(0).getClass().getSimpleName());
            }

            System.out.println("E) Exit");
            int option = Tools.enterOption();
            Account choosenAccount;
            switch (option) {
                case 1:
                    transact(accounts.get(0));
                    break;
                case 2:
                    if (accounts.size() < 2) {
                        System.out.println("Account 2 is not an option");
                    } else {
                        transact(accounts.get(1));
                    }
                    break;
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        } else {
            System.out.println("You dont have any accounts yet,\nCreate at least one from the manage accounts page to be able to create new transactions");
        }
    }

    public static void transact(Account account) throws IOException {
        boolean exit = false;

        while (!exit) {
            System.out.println("hello welcome to transact");
            System.out.println("1) Deposit");
            System.out.println("2) Transfer");
            System.out.println("3) Withdraw");
            System.out.println("E) Exit");
            int option = Tools.enterOption();

            switch (option) {
                case 1:
                    depositShow(account);
                    break;
                case 2:
                    //transfer
                    break;
                case 3:
                    //withdraw
                    break;
                case 100:
                    exit = true;
                    break;
            }
        }
    }

    public static void depositShow(Account account) throws IOException {
        Users user = Session.getLoggedInUser();
        double amount = Tools.enterAmount();
        List<Account> accounts = user.getAccounts();
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ") " + accounts.get(0).getClass().getSimpleName());
            }
            System.out.println("X) Other Account");
            int option = Tools.enterOption();

            switch (option) {
                case 1:
                    TransactionService.deposit(amount, account, accounts.get(0).getAccID());
                    break;
                case 2:
                    TransactionService.deposit(amount, account, accounts.get(1).getAccID());
                    break;
                case 3:
                    int accountID = Tools.enterAccountID();
                    if (accountID != 0) {
                        TransactionService.deposit(amount, account, accountID);
                    }
                    break;
                default:
                    System.out.println("Please enter a valid option");
            }
        }
    }
}
