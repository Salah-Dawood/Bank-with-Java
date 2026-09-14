package com.acme;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionService {
    protected static void deposit(double amount, Account account , int targetAccountID) throws IOException {
        Mastercard card = account.getCard();
        if (isOwnAccount(targetAccountID)) {
            if (getTotalByType("DO") + amount > card.getDepositLimitOW()) {
                System.out.println("Transaction failed:\nAmount entered is past daily limit\n Daily limit available: " + (card.getDepositLimitOW() - getTotalByType("DO")));
                return;
            } else {
                account.setBalance(account.getBalance() + amount);
                //log deposit
                logTransaction("DO",amount);

            }
        } else {
            if (verifyAccount(String.valueOf(targetAccountID))){
                return;
            }
            if (amount > card.getDepositLimit()) {
                System.out.println("Transaction failed:\nAmount entered is past daily limit\nDaily limit to other accounts available: " + card.getDepositLimit());
                return;
                //update target account balance - generate log
            }
        }
    }

    private static boolean isOwnAccount(int targetAccount) {
        String[] userInfo = FileService.getUserLine(Session.getLoggedInUser());
        String[] accounts;
        if (userInfo.length >= 5) {
            accounts = userInfo[4].split(";");
            System.out.println("Accounts: " + Arrays.toString(accounts));
            for (int i = 1; i <= accounts.length; i++) {
                String[] account = accounts[i - 1].split("\\|");
                System.out.println("Account: " + Arrays.toString(account));
                if (targetAccount == Integer.parseInt(account[0])){
                    return true;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public static double getTotalByType(String transactionType) {
        // 1. Build path directly using your established configuration
        Path userFile = FileDBConfig.userFile();

        LocalDate today = LocalDate.now();
        // Prepare the exact prefix to look for at the start of the line (e.g., "DO,")
        String prefixFilter = transactionType.trim() + ",";
        double total = 0;
        // 2. Stream the file lines safely
        try (Stream<String> lines = Files.lines(userFile)) {
            total = lines
                    .filter(line -> line.trim().startsWith(prefixFilter))
                    .mapToDouble(line -> parseAmountIfToday(line, today))
                    .sum();

        } catch (IOException e) {
            System.err.println("Could not read file: " + userFile + " - " + e.getMessage());
            return 0.0;
        }
        System.out.println("total " + transactionType + " today" + total);
        return total;
    }


    private static double parseAmountIfToday(String line, LocalDate today) {
        try {
            String[] parts = line.split(",");

            // Extract and clean the date component (Index 1)
            LocalDate transactionDate = LocalDate.parse(parts[1].trim());

            // If the transaction date matches today, parse and return the primitive double (Index 2)
            if (today.equals(transactionDate)) {
                return Double.parseDouble(parts[2].trim());
            }
        } catch (Exception e) {
            // Skips malformed lines gracefully
            System.err.println("Skipping malformed transaction line: " + line);
        }
        return 0.0; // Return 0.0 if it's not today's transaction or if it fails
    }

    public static boolean logTransaction(String transactionType, Double amount) {
        // 1. Get the path as a standard String from your config file object
        String filePathStr = FileDBConfig.userFile().toString();

        // 2. Format the line exactly like before (TYPE, YYYY-MM-DD, AMOUNT)
        String logEntry = String.format("%s, %s, %s",
                transactionType.trim().toUpperCase(),
                LocalDate.now(),
                amount.toString());

        // 3. Open the file in APPEND MODE using try-with-resources
        // Notice the 'true' parameter inside new FileWriter(..., true).
        // This tells Java to append data rather than overwrite the file!
        try (FileWriter fw = new FileWriter(filePathStr, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(logEntry);
            bw.newLine(); // Automatically adds a new line line break safely

            return true;
        } catch (IOException e) {
            System.err.println("Could not write transaction log: " + e.getMessage());
            return false;
        }
    }

    public static boolean verifyAccount(String targetAccountId) {
        final String searchId = targetAccountId.trim();
        Optional<String> foundUsername;

        // 1. Stream the file to locate the username
        try (Stream<String> lines = Files.lines(FileDBConfig.usersFile)) {
            foundUsername = lines
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 5)
                    .filter(parts -> {
                        String[] accountDetails = parts[4].split("\\|");
                        return accountDetails.length > 0 && accountDetails[0].trim().equals(searchId);
                    })
                    .map(parts -> parts[1].trim())
                    .findFirst();

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return false;
        }

        // 2. Handle missing account layout
        if (foundUsername.isEmpty()) {
            System.out.println("account not found");
            return false;
        }

        // 3. Display exact confirmation choices layout
        System.out.println("confirm account owner " + foundUsername.get());
        System.out.println("1) yes");
        System.out.println("2) no");
        int option = Tools.enterOption();

        switch (option){
            case 1:
                return true;
            case 2:
                return false;
            default:
                System.out.println("Please enter a valid option");
        }
        return false;
    }
}

