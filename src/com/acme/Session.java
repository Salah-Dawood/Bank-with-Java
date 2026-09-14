package com.acme;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Session {

    private static Users loggedInUser;

    public static void login(Users user) throws IOException {
        loggedInUser = user;
        System.out.println("got login call");
        for (int i = 0; i < user.accounts.size();i++) {
            System.out.println("account "+ i + " " + user.accounts.get(i));
            System.out.println("account "+i+" balance: " + FileService.getBalance(i));
            user.accounts.get(i).setBalance(FileService.getBalance(i));
        }
    }

    public static Users getLoggedInUser(){
        return loggedInUser;
    }

    public static boolean logout(){
        if (!Tools.confirm()){
            return false;
        } else{
            loggedInUser = null;
            return true;

        }
    }


}
