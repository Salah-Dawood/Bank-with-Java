package com.acme;

import java.io.*;
import java.util.*;

public class Main {
    private static UserService userService = new UserService();

    public String usersPath = "DB/users.txt";

    public static void main(String[] args) {

        FileDBConfig.initiateDatabase();

        Menu.showMenu();
    }
}
