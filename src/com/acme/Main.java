package com.acme;

import com.acme.auth.Login;
import com.acme.auth.NewCustomer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean quit = false;
        while (!quit){
            System.out.println("Welcome to ACME Bank!");
            printOptions();
            String input = scan.next();
            input = input.replace(" ","");
            if (input.equals("3")){
                quit = true;
            }
            int option = Integer.parseInt(input);
            findOption(option);
        }
    }

    public static void printOptions(){
        System.out.println("1) New Customer");
        System.out.println("2) Login");
        System.out.println("3) Quit");
        System.out.print("Please Enter option: ");
    }

    public static void findOption(int option){
        switch (option){
            case 1:
                NewCustomer.show();
                break;
            case 2:
                Login.show();
                break;
            case 3:
                return;

        }
    }
}
