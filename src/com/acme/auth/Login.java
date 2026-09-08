package com.acme.auth;

import java.io.Console;
import java.util.Scanner;

public class Login {
    public static void show(){
        System.out.println("Login Page:");
        loginInput();
    }

    private static void loginInput(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String name = scan.next();
        System.out.print("Enter Password: ");
        String pass = scan.next();
        System.out.println(pass);
    }
}
