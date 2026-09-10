package com.acme;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileService {

    public static void addUserToUsers(Users user) {
        try (FileWriter fw = new FileWriter(FileDBConfig.usersFile.toString(), true);
             BufferedWriter writer = new BufferedWriter(fw)) {

            writer.write(user.toString() + "," + " ");
            writer.newLine();
        } catch (
                IOException e) {
            System.err.println("Failed to write user to database: " + e.getMessage());
        }
    }

    public static void createUserFile(Users user) {
        Path folder = getUserFilePath(user);
        Path fileName = getUserFileName(user, folder);

        try {
            Files.createDirectories(folder);

            if (Files.notExists(fileName)) {
                Files.createFile(fileName);
                System.out.println("Created profile file: " + fileName.getFileName());
            }
        } catch (IOException e) {
            System.err.println("Failed to create role profile file for " + user.userName + ": " + e.getMessage());
        }
    }

    public static void addAccountToUser(String newData) {
        Users user = Session.getLoggedInUser();
        String [] accounts = getUserAccountsInfo(user);
        ArrayList<String> accountList = new ArrayList<>(Arrays.asList(accounts));


        if (accountList.size() == 1 && accountList.get(0).trim().isEmpty()) {
            // Remove the blank " " string placeholder so it doesn't stay in your list
            accountList.remove(0);
        }

        // 4. Simply call .add()! The ArrayList grows automatically.
        accountList.add(newData);

        System.out.println("ArrayList content: " + accountList);

        // 5. OPTIONAL: Convert it back to a raw String[] array for your system
        String[] finalArray = accountList.toArray(new String[0]);
        System.out.println(finalArray);

    }

    private static Path getUserFilePath(Users user) {
        Path folder;

        if ("Banker".equalsIgnoreCase(user.getType())) {
            folder = FileDBConfig.bankersPath;
            return folder;
        } else if ("Customer".equalsIgnoreCase(user.getType())) {
            folder = FileDBConfig.customersPath;
            return folder;
        } else {
            System.err.println("failed to resolve user type = " + user.getType() + " for user " + user.getUserName());
            return Path.of("");
        }
    }

    private static Path getUserFileName(Users user, Path folder) {
        user.getType().toString();
        return folder.resolve(String.format("%s-%s-%s.txt", user.getType(), user.getFirstName(), user.getUserName()));
    }

    public static String[] getUserAccountsInfo(Users user) {
        String[] userInfo = getUserLine(user);
        String[] accounts = userInfo[4].split(";");
        System.out.println(accounts);

        return accounts;
    }

    public static String[] getUserLine(Users user){
        System.out.println();
        String folderName = "DB";
        String fileName = "users.txt";
        String userLine = "";
        Path path = Paths.get(folderName, fileName);

        try (Stream<String> lineStream = Files.lines(path)) {

            userLine = lineStream.filter(line -> line.contains(user.getUserName()))
                    .collect(Collectors.joining(""));
            System.out.println(userLine);

        } catch (IOException e) {
            System.out.println("Could not read the file: " + e.getMessage());

        }
        System.out.println(Arrays.toString(userLine.split(",")));
        return userLine.split(",");
    }

    public static void newUserLine(Users user,String newLine){

        String userLine = user.getUserName();
        List<String> updatedLines;

        try (Stream<String> lineStream = Files.lines(FileDBConfig.usersFile)) {

            updatedLines = lineStream.map(line -> {
                if (line.contains(user.getUserName())) {
                    return newLine;
                }
                return line;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        try {
            Files.write(FileDBConfig.usersFile, updatedLines);
            System.out.println("Line replaced successfully!");
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}

