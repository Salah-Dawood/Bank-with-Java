package com.acme;

import java.io.IOException;

public class AccountServices {

    public static void show() throws IOException {
        Users user = Session.getLoggedInUser();
        boolean quit = false;
        while (!quit){
            System.out.println("Manage Accounts");
            System.out.println("1) Create Account");
            System.out.println("2) View accounts");
            System.out.println("E) Exit");
            System.out.print("Enter option: ");
            int option = Tools.enterOption();
            switch (option) {
                case 1:
                    //create account prompt
                    createAccount(user);
                    break;
                case 2:
                    FileService.displayUserAccounts(user);
                    break;
                case 100:
                    quit = true;
                    break;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        }
    }

    protected static void createAccount(Users user) throws IOException {
        System.out.println("Select type of account.");
        System.out.println("1) Checking Account");
        System.out.println("2) Savings Account");
        int option = Tools.enterOption();

        switch (option){
            case 1:
                System.out.println("Creating Checking account");
                user.createChecking();
                break;
            case 2:
                System.out.println("Creating Savings account");
                user.createSavings();
                break;
            default:
                System.out.println("Please Enter valid option");
                break;
        }
    }
}
