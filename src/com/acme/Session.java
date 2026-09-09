package com.acme;

import java.util.Scanner;

public class Session {

    private static Users loggedInUser;

    public static void login(Users user){
        loggedInUser = user;
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
