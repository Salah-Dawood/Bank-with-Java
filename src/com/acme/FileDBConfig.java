package com.acme;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileDBConfig {
    public static final Path rootDir = Paths.get("DB");
    public static final Path usersFile = rootDir.resolve("users.txt");
    public static final Path bankersPath = Paths.get(String.valueOf(rootDir),"Bankers");
    public static final Path customersPath = Paths.get(String.valueOf(rootDir),"Customers");

    //clear root

    public static void initiateDatabase() {

        try (FileWriter fw = new FileWriter(FileDBConfig.usersFile.toString(), false);
             BufferedWriter writer = new BufferedWriter(fw)) {

        } catch (IOException e) {
            System.err.println("An error occurred " + e.getMessage());
        }

        // Define the target paths
        Path customersPath = Paths.get("DB", "Customers");
        Path bankersPath = Paths.get("DB", "Bankers");

        try {
            // Files.createDirectories creates the target folder and any missing parent folders (like DB/)
            Files.createDirectories(customersPath);
            System.out.println("Created: " + customersPath.toAbsolutePath());

            Files.createDirectories(bankersPath);
            System.out.println("Created: " + bankersPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Failed to create directories: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
