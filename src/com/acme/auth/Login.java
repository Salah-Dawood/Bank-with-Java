package com.acme.auth;

import java.util.Scanner;

public class Login {
    public static void show(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Login as..");
        System.out.println("1) Customer");
        System.out.println("2) Banker");
        System.out.print("Enter choice:");
        String input = scan.next().replace(" ","");
        if (input.equals("3")){
            return;
        }
        int choice = Integer.parseInt(input);
        switch (choice){
            case 1:
                customerLoginInput();
                break;
            case 2:
                bankerLoginInput();
                break;
            case 3:
                break;
        }

    }

    private static void customerLoginInput(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Customer Login");
        System.out.print("Enter Username: ");
        String name = scan.next();
        System.out.print("Enter Password: ");
        String pass = scan.next();

    }

    private static void bankerLoginInput(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Banker Login");
        System.out.print("Enter Username: ");
        String name = scan.next();
        System.out.print("Enter Password: ");
        String pass = scan.next();
    }
}
