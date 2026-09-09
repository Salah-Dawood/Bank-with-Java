package com.acme;

import com.acme.dashboard.CheckingAccount;

import java.util.Scanner;
import java.util.function.ToLongBiFunction;

public class AccountServices {

    public static void show(){
        Users user = Session.getLoggedInUser();
        boolean quit = false;
        while (!quit){
            System.out.println("Manage Accounts");
            System.out.println("1) Create Account");
            System.out.println("2) Quit");
            System.out.print("Enter option: ");
            int option = Tools.enterOption();
            switch (option) {
                case 1:
                    //create account prompt
                    createAccount(user);
                    break;
                case 2:
                    quit = true;
                    break;
            }
        }
    }

    protected static void createAccount(Users user){
        System.out.println("Select type of account.");
        System.out.println("1) Checking Account");
        System.out.println("2) Savings Account");
        int option = Tools.enterOption();

        switch (option){
            case 1:
                System.out.println("checking account");
                break;
            case 2:
                System.out.println("savings account");
                break;
            default:
                System.out.println("Please Enter valid option");
                break;
        }
    }
}
