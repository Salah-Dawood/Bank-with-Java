package com.acme;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
            if (account.length < 2){
                System.out.println(Arrays.toString(account));
                return;
            }
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


    public static void updateUserLine(Users user) throws IOException {
        // Create a temporary file in the same directory
        Path tempFile = Files.createTempFile(FileDBConfig.usersFile.getParent(), "temp_", ".txt");

        // Define what the unique identifier
        String targetIdentifier = user.getUserName();

        String updatedLineData = user.toString();

        // open files for reading and writing
        try (Stream<String> lines = Files.lines(FileDBConfig.usersFile);
             BufferedWriter writer = Files.newBufferedWriter(tempFile)) {

            lines.forEach(line -> {
                try {
                    if (line.contains(targetIdentifier)) {
                        writer.write(updatedLineData);
                    } else {
                        writer.write(line);
                    }
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException("Error writing to temporary file", e);
                }
            });

        } catch (RuntimeException e) {
            //clean up
            Files.deleteIfExists(tempFile);
            throw new IOException("File update failed", e.getCause());
        }

        Files.move(tempFile, FileDBConfig.usersFile, StandardCopyOption.REPLACE_EXISTING);
    }


    public static double getBalance(int i){
        Users user = Session.getLoggedInUser();
        String[] accounts = getUserAccountsInfo(user);
        String[] account = accounts[i].split("\\|");
        System.out.println("returning balance: "+account[2]);
        return Double.parseDouble(account[2]);

    }

    public static String[] getAccount(int id) {
        try (Stream<String> lines = Files.lines(FileDBConfig.usersFile)) {
            return lines
                    .map(line -> line.split(",", 5))
                    .filter(parts -> parts.length == 5)
                    .flatMap(parts -> Stream.of(parts[4].split(";")))
                    .map(acc -> acc.split("\\|"))
                    .filter(fields -> fields.length > 0 && fields[0].equals(String.valueOf(id)))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read users file", e);
        }
    }

    public static boolean updateBalance(int id, double amount) {
        List<String> originalLines;
        try {
            originalLines = Files.readAllLines(FileDBConfig.usersFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read users file", e);
        }

        boolean[] found = {false}; // effectively-final holder so the lambda can flag a match

        List<String> updatedLines = originalLines.stream()
                .map(line -> {
                    String[] userParts = line.split(",", 5);
                    if (userParts.length != 5) {
                        return line; // malformed line, leave untouched
                    }

                    String updatedAccountsBlob = Arrays.stream(userParts[4].split(";"))
                            .map(acc -> {
                                String[] fields = acc.split("\\|");
                                if (fields.length > 0 && fields[0].equals(String.valueOf(id))) {
                                    found[0] = true;
                                    fields[2] = String.valueOf(Double.parseDouble(fields[2]) + amount);
                                    return String.join("|", fields);
                                }
                                return acc;
                            })
                            .collect(Collectors.joining(";"));

                    return String.join(",", userParts[0], userParts[1], userParts[2], userParts[3], updatedAccountsBlob);
                })
                .collect(Collectors.toList());

        if (!found[0]) {
            return false;
        }

        try {
            Files.write(FileDBConfig.usersFile, updatedLines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write users file", e);
        }

        return true;
    }
}

