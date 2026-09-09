package com.acme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Tools {
    public static void space(int t){
        for (int i = 0;i<=t;i++){
            System.out.println();
        }
    }

    public static boolean confirm(){
        System.out.println("Are you sure?\n1) Yes\n2) No");
        int option = Tools.enterOption();
        switch (option){
            case 1:
                return true;
            case 2:
                return false;
            default:
                System.out.println("Please enter a valid option");
                return false;
        }
    }

    public static int enterOption(){
        System.out.print("Enter option: ");
        String input = new Scanner(System.in).next();
        input = input.replace(" ","");
        int option;
        try {
            option = Integer.parseInt(input);
        } catch (NumberFormatException e){
            option = 0;
        }
        return option;
    }

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return now.format(formatter);
    }
}
