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
            System.out.println("2) get accounts");
            System.out.println("3) new acc");
            System.out.println("4) Logout");
            int option = Tools.enterOption();

            switch (option) {
                case 1:
                    AccountServices.show();
                    break;
                case 2:
                    FileService.getUserAccountsInfo(user);
                    break;
                case 3:
                    FileService.newUserLine(user,"Customer,Mohamed,moham123,Mohammed,[]");
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
}
