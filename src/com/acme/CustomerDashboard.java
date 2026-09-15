package com.acme;


import java.io.IOException;
import java.util.Scanner;

public class CustomerDashboard implements IDashboard {

    private AccountServices accountServices = new AccountServices();

    public void show() throws IOException {
        Users user = Session.getLoggedInUser();

        Tools.space(16);
        boolean logout = false;
        while (!logout) {
            System.out.println("Welcome " + user.getFirstName() +"\nWhat service do you need?");
            System.out.println("1) Manage Accounts");
            System.out.println("2) New Transaction");
            System.out.println("3) Change Password");
            System.out.println("4) Logout");
            int option = Tools.enterOption();

            switch (option) {
                case 1:
                    AccountServices.show();
                    break;
                case 2:
                    TransactionsDashboard.show();
                    break;
                case 3:
                    changePasswordShow(user);
                    break;
                case 4:
                    logout = Session.logout();
                    break;
                default:
                    System.out.println("Please enter a valid option.");
            }
        }
        Tools.space(16);
    }

    public void changePasswordShow(Users user) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter new password: ");
        String newPass = scan.next();
        if (UserService.changePassword(newPass,user)){
            System.out.println("Password Changed!");
        } else {
            System.out.println("Password Change Failed!");
        }
    }
}
