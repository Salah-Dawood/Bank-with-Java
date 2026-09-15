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
                System.out.println((i + 1) + ") " + accounts.get(i).getClass().getSimpleName());
            }

            System.out.println("E) Exit");
            int option = Tools.enterOption();
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
                    transferShow(account);
                    break;
                case 3:
                    //withdraw
                    withdrawShow(account);
                    break;
                case 100:
                    exit = true;
                    break;
            }
        }
    }

    private static void transferShow(Account account) throws IOException {
        Users user = account.getUser();
        double amount = Tools.enterAmount();
        listAccounts(user);
        List<Account> accounts = user.getAccounts();
        int option = Tools.enterOption();

        switch (option){
            case 1:
                TransactionService.transfer(amount, account, accounts.get(0).getAccID());
                break;
            case 2:
                if (accounts.size() >= 2) {
                    TransactionService.transfer(amount, account, accounts.get(1).getAccID());
                    break;
                } else {
                    System.out.println("Please enter a valid option");
                }
            case 99:
                int accountID = Tools.enterAccountID();
                if (accountID > 999 && accountID < 10000) {
                    TransactionService.transfer(amount, account, accountID);
                } else {
                    System.out.println("Account ID must consist of a 4 digit number");
                }
                break;

            default:
                System.out.println("Please enter a valid option");
        }

    }

    public static void depositShow(Account account) throws IOException {
        Users user = account.getUser();
        double amount = Tools.enterAmount();
        listAccounts(user);
        List<Account> accounts = user.getAccounts();
        int option = Tools.enterOption();
        switch (option) {
            case 1:
                TransactionService.deposit(amount, account, accounts.get(0).getAccID());
                break;
                case 2:
                    if (accounts.size() >= 2) {
                        TransactionService.deposit(amount, account, accounts.get(1).getAccID());
                        break;
                    } else {
                        System.out.println("Please enter a valid option");
                    }
                case 99:
                    int accountID = Tools.enterAccountID();
                    if (accountID > 999 && accountID < 10000) {
                        TransactionService.deposit(amount, account, accountID);
                    } else {
                        System.out.println("Account ID must consist of a 4 digit number");
                    }
                    break;

                default:
                    System.out.println("Please enter a valid option");
            }
        }

        public static void withdrawShow(Account account) throws IOException {
            Users user = account.getUser();
            double amount = Tools.enterAmount();
            TransactionService.withdraw(amount,account);
        }

    public static void listAccounts(Users user){
        List<Account> accounts = user.getAccounts();
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ") " + accounts.get(i).getClass().getSimpleName());
            }
            System.out.println("X) Other Account");
        }
    }
}
