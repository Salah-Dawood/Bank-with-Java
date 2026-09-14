package com.acme;

import com.acme.Users;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class UserService {

    private List<Users> users = new ArrayList<>();

    public void initialUsers(){
        System.out.println("initializing users");
        addUser(new Banker("admin","admin123","Adam"));
        addUser(new Customer("Moham","moham123","Mohammed"));
    }
    public boolean addUser(Users user) {
        if (!isFirstNameValid(user.firstName)){
            System.out.println("First name must only contain English letters!\nExample: John");
            return false;
        }
        if (!isUserNameValid(user.userName)){
            System.out.println("Username must only contain English letter and numbers!\nExample: John123");
            return false;
        }
        if (isUsernameTaken(user.userName)){
            System.out.println("Username already taken!\nPlease try again with another username.");
            return false;
        }
        users.add(user);
        FileService.addUserToUsers(user);
        FileService.createUserFile(user);
        return true;
    }

    public Users login(String userName, String password) {
        
        try (Stream<String> lineStream = Files.lines(FileDBConfig.usersFile)) {

            return lineStream
                    .map(line -> line.split(","))

                    .filter(parts -> parts[1].equals(userName) && parts[2].equals(password))

                    .flatMap(parts -> users.stream()
                            .filter(user -> user.getUserName().equalsIgnoreCase(userName) && user.getPassword().equals(password)))

                    .findFirst()
                    .orElse(null);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected boolean isUsernameTaken(String userName) {

        try (FileReader fr = new FileReader(FileDBConfig.usersFile.toString());
             BufferedReader reader = new BufferedReader(fr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                String existingUserName = parts[0];

                if (existingUserName.equalsIgnoreCase(userName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading database: " + e.getMessage());
        }
        return false;
    }

    private boolean isFirstNameValid(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return false;
        }
        //matches letters a-z upper or lower case
        return firstName.matches("^[A-Za-z]+$");
    }

    private boolean isUserNameValid(String userName) {
        if (userName == null || userName.isEmpty()) {
            return false;
        }
        //matches upper and lower case from a-z and numbers 0-9
        return userName.matches("^[A-Za-z0-9]+$");
    }

    //loading accounts to user

    public static Optional<Account> loadUserAccountOnLogin(String loggedInUsername) {

        try (Stream<String> lines = Files.lines(FileDBConfig.usersFile)) {
            return lines
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 5)
                    // Filter by the matching logged-in username (index 1)
                    .filter(parts -> parts[1].trim().equals(loggedInUsername.trim()))
                    // Extract the pipe-separated account block (index 4) and build the object
                    .map(parts -> createAccountFromData(parts[4]))
                    .filter(java.util.Objects::nonNull)
                    .findFirst();

        } catch (IOException e) {
            System.err.println("Database error loading user session: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Instantiates an account using the no-arg constructor and populates its fields via setters.
     * Input layout: "8039|CheckingAccount|150.0|50.0|true|Mastercard"
     */
    private static Account createAccountFromData(String accountDataChunk) {
        try {
            String[] details = accountDataChunk.split("\\|");
            System.out.println("Account found" + Arrays.toString(details));
            if (details.length < 6) return null;

            // 1. Parse all individual strings into their raw variable types
            int accID = Integer.parseInt(details[0].trim());
            String accountType = details[1].trim();
            double balance = Double.parseDouble(details[2].trim());
            double overDraftTotal = Double.parseDouble(details[3].trim());
            boolean isActive = Boolean.parseBoolean(details[4].trim());
            // Assumes Mastercard constructor handles its setup or has a fallback string handler
            String cardType = details[5].trim();

            Account account;

            // 2. Instantiate using your no-argument constructor (throws IOException)
            switch (accountType) {
                case "CheckingAccount":
                    account = new CheckingAccount();
                    break;
                case "SavingsAccount":
                    account = new SavingsAccount();
                    break;
                default:
                    System.err.println("Unknown account type: " + accountType);
                    return null;
            }

            // 3. Overwrite the generated constructor values with data parsed from the file
            account.setAccID(accID);
            account.setBalance(balance);
            account.setOverDraftTotal(overDraftTotal);
            account.setActive(isActive);

            // Note: If you have a setter that takes a string or card object, inject it here
            // account.setCard(new Mastercard(cardType));

            return account;

        } catch (IOException e) {
            System.err.println("IOException occurred while running Account constructor: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Failed parsing account attributes: " + e.getMessage());
            return null;
        }
    }

}