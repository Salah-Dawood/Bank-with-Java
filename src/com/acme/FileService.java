package com.acme;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {

    public static void addUserToUsers(Users user){
        try (FileWriter fw = new FileWriter(FileDBConfig.usersFile.toString(), true);
             BufferedWriter writer = new BufferedWriter(fw)) {

            writer.write(user.toString());
            writer.newLine();
        }catch (
                IOException e) {
            System.err.println("Failed to write user to database: " + e.getMessage());
        }
    }

    public static void createUserFile(Users user) {
        Path folder;
        Path fileName;

        if ("Banker".equalsIgnoreCase(user.getType())) {
            folder = FileDBConfig.bankersPath;
            fileName = folder.resolve(String.format("Banker-%s-%s.txt", user.getFirstName(), user.getUserName()));
        } else if ("Customer".equalsIgnoreCase(user.getType())) {
            folder = FileDBConfig.customersPath;
            fileName = folder.resolve(String.format("Customers-%s-%s.txt", user.getFirstName(), user.getUserName()));
        } else {
            System.err.println("failed to resolve user type = " + user.getType() + " for user " + user.getUserName());
            return;
        }

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
}
