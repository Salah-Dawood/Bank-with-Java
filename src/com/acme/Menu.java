package com.acme;

import java.util.Scanner;

public class Menu {

    private static UserService userService = new UserService();
    private static CustomerDashboard customerDashboard = new CustomerDashboard();
    private static BankerDashboard bankerDashboard = new BankerDashboard();

    public static void showMenu(){

        userService.initialUsers();
        Scanner scan = new Scanner(System.in);

        boolean quit = false;
        while (!quit){
            System.out.println("Welcome to ACME Bank!");
            System.out.println("1) New Customer");
            System.out.println("2) Login");
            System.out.println("3) Quit");
            int option = Tools.enterOption();

            Tools.space(16);
            switch (option){
                case 1:
                    newCustomerInput();
                    break;
                case 2:
                    loginInput();
                    break;
                case 3:
                    System.out.println("Bye Bye");
                    break;
                default:
                    System.out.println("Please Enter a Valid Option!");
        }
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
        if (!userService.isUsernameTaken(newName)){
            userService.addUser(new Customer(newName, newPass, firstName));
        } else {
            System.out.println("Username already taken,\nPlease try again with another username.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("The sleep was interrupted.");
        }}
    }

    private static void loginInput(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter credentials");
        System.out.print("Enter Username: ");
        String name = scan.next();
        System.out.print("Enter Password: ");
        String pass = scan.next();

        Users loggedInUser = userService.login(name, pass);

        if (loggedInUser != null) {
            Session.login(loggedInUser);
            System.out.println("Login successful!");

            if (loggedInUser instanceof Customer) {
                //DISPLAY C DASHBOARD HERE
                customerDashboard.show();
            }
            else if (loggedInUser instanceof Banker) {
                //DISPLAY B DASHBOARD HERE
                bankerDashboard.show();
            }

        } else {
            System.out.println("Invalid username or password");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("The sleep was interrupted.");
            }
        }
    }
}

