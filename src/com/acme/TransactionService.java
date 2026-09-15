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
            System.out.println("ITS OWN ACCOUNT");
            if (getTotalByType("DO",account.getAccID()) + amount > card.getDepositLimitOW()) {
                System.out.println("Transaction failed:\nAmount entered is past daily limit\n Daily limit available: " + (card.getDepositLimitOW() - getTotalByType("DO",account.getAccID())));
                return;
            } else {
                account.setBalance(account.getBalance() + amount);
                //log deposit
                logDeposit("DO",amount,targetAccountID);

            }
        } else {
            if (!verifyAccount(String.valueOf(targetAccountID))){
                return;
            }
            if (getTotalByType("OD",account.getAccID()) + amount > card.getDepositLimit()) {
                System.out.println("Transaction failed:\nAmount entered is past daily limit\nDaily limit to other accounts available: " + (card.getDepositLimit() - getTotalByType("OD",account.getAccID())));
                return;

            } else {
                if (FileService.updateBalance(targetAccountID,amount)){
                    logDeposit("OD",amount,account.getAccID());
                    logDeposit("D",amount,targetAccountID);
                    System.out.println("Seccess");
                }
            }
        }
    }

    private static boolean isOwnAccount(int targetAccount) {
        String[] userInfo = FileService.getUserLine(Session.getLoggedInUser());

        if (userInfo.length < 5) {
            return false;
        }

        return Arrays.stream(userInfo[4].split(";"))
                .map(acc -> acc.split("\\|"))
                .anyMatch(account -> targetAccount == Integer.parseInt(account[0]));
    }

    public static double getTotalByType(String transactionType,int accID) {
        // 1. Build path directly using your established configuration
        Path userFile = Path.of(FileDBConfig.userFile(accID));

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

    public static boolean logTransaction(String logEntry,int accID) {

        String filePathStr = FileDBConfig.userFile(accID).toString();

        try (FileWriter fw = new FileWriter(filePathStr, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(logEntry);
            bw.newLine();

            return true;
        } catch (IOException e) {
            System.err.println("Could not write transaction log: " + e.getMessage());
            return false;
        }
    }

    public static boolean verifyAccount(String targetAccountId) {
        final String searchId = targetAccountId.trim();
        Optional<String> foundUsername;

        try (Stream<String> lines = Files.lines(FileDBConfig.usersFile)) {
            foundUsername = lines
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 5)
                    .filter(parts -> Arrays.stream(parts[4].split(";"))
                            .map(acc -> acc.split("\\|"))
                            .anyMatch(accountDetails -> accountDetails.length > 0 && accountDetails[0].trim().equals(searchId)))
                    .map(parts -> parts[1].trim())
                    .findFirst();

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return false;
        }

        if (foundUsername.isEmpty()) {
            System.out.println("account not found");
            return false;
        }

        System.out.println("confirm account owner " + foundUsername.get());
        System.out.println("1) yes");
        System.out.println("2) no");
        int option = Tools.enterOption();

        switch (option) {
            case 1:
                return true;
            case 2:
                return false;
            default:
                System.out.println("Please enter a valid option");
        }
        return false;
    }

    public static void transfer(double amount,Account account,int accID) throws IOException {
        Mastercard card = account.getCard();
        if (isOwnAccount(accID)) {
            System.out.println("ITS OWN ACCOUNT");
            if (getTotalByType("TO",account.getAccID()) + amount > card.getTransferLimitOW()) {
                System.out.println("Transaction failed:\nAmount entered is past daily limit\n Daily limit available: " + (card.getTransferLimitOW() - getTotalByType("TO",account.getAccID())));
                return;
            } else {
                account.setBalance(account.getBalance() - amount);
                FileService.updateBalance(accID,amount);
                Session.updateBalance(account.getUser());
                //log deposit
                logTrasferSource("TO",amount,account.getAccID(),accID);

            }
        } else {
            if (!verifyAccount(String.valueOf(accID))){
                return;
            }
            if (getTotalByType("OT",account.getAccID()) + amount > card.getTransferLimit()) {
                System.out.println("Transaction failed:\nAmount entered is past daily limit\nDaily limit to other accounts available: " + (card.getDepositLimit() - getTotalByType("OD",account.getAccID())));
                return;

            } else {
                if (FileService.updateBalance(accID,amount)){
                    account.setBalance(account.getBalance() - amount);
                    logTrasferSource("OT",amount,account.getAccID(),accID);
                    logTrasferDestination("T",amount, account.getAccID(), accID);
                    System.out.println("Seccess");
                }
            }
        }
    }

    public static void withdraw(double amount, Account account) throws IOException {
        if (getTotalByType("W",account.getAccID()) + amount > account.getCard().getWithdrawLimit()) {
            System.out.println("Transaction failed:\nAmount entered is past daily limit\n Daily limit available: " + (account.getCard().getWithdrawLimit() - getTotalByType("W",account.getAccID())));
            return;
        } else {
            account.setBalance(account.getBalance() - amount);
            logWithdraw(amount,account.getAccID());
        }
    }

    public static void logDeposit(String transactionType, Double amount, int accID){
        String logEntry = String.format("%s, %s, %s",
                transactionType.trim().toUpperCase(),
                LocalDate.now(),
                amount.toString());
        logTransaction(logEntry, accID);
    }

    public static void logTrasferSource(String transactionType, Double amount, int sourceAcc,int destAcc){
        String logEntry = String.format("%s, %s, %s, %s, %s",
                transactionType.trim().toUpperCase(),
                LocalDate.now(),
                amount.toString(),
                String.valueOf(sourceAcc),
                String.valueOf(destAcc));
        logTransaction(logEntry, sourceAcc);
    }
    public static void logTrasferDestination(String transactionType, Double amount, int sourceAcc,int destAcc){
        String logEntry = String.format("%s, %s, %s, %s, %s",
                transactionType.trim().toUpperCase(),
                LocalDate.now(),
                amount.toString(),
                String.valueOf(sourceAcc),
                String.valueOf(destAcc));
        logTransaction(logEntry, destAcc);
    }

    public static void logWithdraw(Double amount, int accID){
        String logEntry = String.format("W, %s, %s, %s",
                LocalDate.now(),
                amount.toString(),
                String.valueOf(accID));
        logTransaction(logEntry, accID);
    }
}

