package com.acme;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

            writer.write(user.toString() + ",");
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

    public static void addAccountToUser(String username, String newAccountEntry) throws IOException {
        Path path = FileDBConfig.usersFile;
        List<String> lines = Files.readAllLines(path);
        List<String> updated = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",", 5);
            if (parts.length == 5 && parts[1].equals(username)) {
                String accountsPart = parts[4];
                System.out.println("Accounts part: " + accountsPart);
                accountsPart = accountsPart.isEmpty()
                        ? newAccountEntry
                        : accountsPart + ";" + newAccountEntry;
                line = String.join(",", parts[0], parts[1], parts[2], parts[3], accountsPart);
            }
            updated.add(line);
        }

        Files.write(path, updated); // overwrites the whole file with new content
    }

    private static Path getUserFilePath(Users user) {
        Path folder;

        if ("Banker".equalsIgnoreCase(user.getClass().getSimpleName())) {
            folder = FileDBConfig.bankersPath;
            return folder;
        } else if ("Customer".equalsIgnoreCase(user.getClass().getSimpleName())) {
            folder = FileDBConfig.customersPath;
            return folder;
        } else {
            System.err.println("failed to resolve user type = " + user.getClass().getSimpleName() + " for user " + user.getUserName());
            return Path.of("");
        }
    }

    private static Path getUserFileName(Users user, Path folder) {
//        user.getType().toString();
        return folder.resolve(String.format("%s-%s-%s.txt", user.getClass().getSimpleName(), user.getFirstName(), user.getUserName()));
    }

    public static String[] getUserAccountsInfo(Users user) {
        String[] userInfo = getUserLine(user);
        String[] accounts;
        if (userInfo.length >= 5) {
            accounts =  userInfo[4].split(";");
        } else {
            System.out.println("No accounts found");
            accounts = new String[]{};
        }
        return accounts;
    }

    public static void displayUserAccounts(Users user){
        String[] accounts = getUserAccountsInfo(user);
        for (int i = 0; i < accounts.length;i++){
            String[] account = accounts[i].split("\\|");
            System.out.println(Arrays.toString(account));
            System.out.println("Account Type: " + account[1]);
            System.out.println("ID: " + account[0]);
            System.out.println("Balance: " + account[2]);
            System.out.println("Overdraft fees: " + account[3]);
            System.out.println("is Active: " + account[4]);
            System.out.println("Card type: " + account[5]);
        }
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
            //System.out.println(userLine);

        } catch (IOException e) {
            System.out.println("Could not read the file: " + e.getMessage());

        }
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

    public static void updateAccountBalance(){

    }

    public static void updateUserLine(Users user) throws IOException {
        // Create a temporary file in the same directory
        Path tempFile = Files.createTempFile(FileDBConfig.usersFile.getParent(), "temp_", ".txt");

        // Define what the unique identifier is (e.g., User ID or Account Number)
        String targetIdentifier = user.getUserName();

        // Generate the brand new line data using polymorphism
        // This automatically calls Savings.toString() or Checking.toString()
        String updatedLineData = user.toString();

        // Open the original file for reading and the temp file for writing
        try (Stream<String> lines = Files.lines(FileDBConfig.usersFile);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {

            lines.forEach(line -> {
                try {
                    // Check if this is the line belonging to the user
                    if (line.contains(targetIdentifier)) {
                        // Write the updated information instead of the old line
                        writer.write(updatedLineData);
                    } else {
                        // Keep the existing line exactly as it was
                        writer.write(line);
                    }
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException("Error writing to temporary file", e);
                }
            });

        } catch (RuntimeException e) {
            // Clean up the temp file if something went wrong during processing
            Files.deleteIfExists(tempFile);
            throw new IOException("File update failed", e.getCause());
        }

        // Atomically replace the old file with the updated temporary file
        Files.move(tempFile, FileDBConfig.usersFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static double getBalance(int i){
        Users user = Session.getLoggedInUser();
        String[] accounts = getUserAccountsInfo(user);
        String[] account = accounts[i].split("\\|");
        return Double.parseDouble(account[2]);

    }
}

