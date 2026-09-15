package com.acme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public final class FileDBConfig {
    public static final Path rootDir = Paths.get("DB");
    public static final Path usersFile = rootDir.resolve("users.txt");
    public static final Path bankersPath = Paths.get(String.valueOf(rootDir),"Bankers");
    public static final Path customersPath = Paths.get(String.valueOf(rootDir),"Customers");

    //clear root
    public static String userFile(int accId) {
        try (Stream<String> lines = Files.lines(usersFile)) {
            return lines
                    .map(line -> line.split(",", 5))
                    .filter(parts -> parts.length == 5)
                    .filter(parts -> Arrays.stream(parts[4].split(";"))
                            .map(acc -> acc.split("\\|"))
                            .anyMatch(fields -> fields.length > 0 && fields[0].equals(String.valueOf(accId))))
                    .findFirst()
                    .map(parts -> {
                        String role = parts[0]; // "Customer" or "Banker"
                        String folder = role.equalsIgnoreCase("Customer") ? "Customers" : "Bankers";
                        String filename = role + "-" + parts[3] + "-" + parts[1] + ".txt";
                        return "DB" + File.separator + folder + File.separator + filename;
                    })
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read users file", e);
        }
    }

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
