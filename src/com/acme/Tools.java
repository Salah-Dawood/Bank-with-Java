package com.acme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Tools {
    public static void space(int t){
        for (int i = 0;i<=t;i++){
            System.out.println();
        }
    }

    public static boolean confirm(){
        System.out.println("Are you sure?\n1) Yes\n2) No");
        int option = Tools.enterOption();
        switch (option){
            case 1:
                return true;
            case 2:
                return false;
            default:
                System.out.println("Please enter a valid option");
                return false;
        }
    }

    public static int enterOption(){
        System.out.print("Enter option: ");
        String input = new Scanner(System.in).next();
        input = input.replace(" ","");
        int option;
        if (input.equalsIgnoreCase("e")){
            option = 100;
            return option;
        } else if (input.equalsIgnoreCase("x")) {
            option = 99;
            return option;
        }
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e){
            option = 0;
        }
        return option;
    }

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return now.format(formatter);
    }

    public static void wait(int seconds){
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            System.out.println("The sleep was interrupted.");
        }
    }

    public static double enterAmount(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter amount: ");
        String input = scan.next();
        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
            amount = 0;
        }
        return amount;
    }

    public static int enterAccountID(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter Account ID: ");
        String input = scan.next();
        int accountID;
        if (input.length() == 4){
            try {
                accountID = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Invalid account ID, ID must consist of a four digit number");
                return 0;
            }
            return accountID;
        }
        System.out.println("Invalid account ID, ID must consist of a four digit number");
        return 0;
    }
}
