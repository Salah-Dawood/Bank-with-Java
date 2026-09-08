package com.acme;

import com.acme.auth.Login;
import com.acme.auth.Menu;
import com.acme.auth.NewCustomer;
import com.acme.dashboard.Banker;
import com.acme.dashboard.Customer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //ADMIN BANKER
        Banker admin = new Banker("admin", "password123", "Ali", "Adam");

        //TESTING CUSTOMER
        Customer customer1 = new Customer("Moham","moham123","Mohammed","Mahmood");
        Menu.showMenu();
    }
}
