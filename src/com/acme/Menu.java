package com.acme;

import com.acme.auth.Login;
import com.acme.auth.NewCustomer;

import java.util.Scanner;

public class Menu {

    private static UserService userService = new UserService();

    public static void showMenu(){

        userService.initialUsers();
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
        Tools.space(16);
        switch (option){
            case 1:
                newCustomerInput();
                break;
            case 2:
                loginInput();
                break;
            case 3:
                break;


        }
    }

    private static void newCustomerInput() {
        System.out.println("Create New Customer");
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter First Name: ");
        String firstName = scan.next();

        System.out.print("Enter new username: ");
        String newName = scan.next();

        System.out.print("Enter new password: ");
        String newPass = scan.next();

        userService.addUser(new Customer(newName, newPass, firstName)
        );
    }

    private static void loginInput(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Customer Login");
        System.out.print("Enter Username: ");
        String name = scan.next();
        System.out.print("Enter Password: ");
        String pass = scan.next();

        Users loggedInUser = userService.login(name, pass);

        if (loggedInUser != null) {

            System.out.println("Login successful!");

            if (loggedInUser instanceof Customer) {
                System.out.println("Welcome Customer");
            }
            else if (loggedInUser instanceof Banker) {
                System.out.println("Welcome Banker");
            }

        } else {
            System.out.println(name + " " + pass);
            System.out.println("Invalid username or password");
        }
    }
}

