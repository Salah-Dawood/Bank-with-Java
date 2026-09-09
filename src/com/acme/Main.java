package com.acme;

import java.io.*;
import java.util.*;

public class Main {
    private static UserService userService = new UserService();

    public String usersPath = "DB/users.txt";

    public static void main(String[] args) {

        try (FileWriter fw = new FileWriter(FileDBConfig.usersFile.toString(), false);
             BufferedWriter writer = new BufferedWriter(fw)) {

        } catch (IOException e) {
            System.err.println("An error occurred " + e.getMessage());
        }

        Menu.showMenu();
    }
}
