package com.acme.auth;

import java.util.Scanner;

public class NewCustomer {
    public static void show(){
        System.out.println("New customer page");
        newCustomerInput();
    }

    private static void newCustomerInput(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter new username: ");
        String newName = scan.next();
        System.out.print("Enter new password: ");
        String newPass = scan.next();
        System.out.println(newPass);
    }
}
