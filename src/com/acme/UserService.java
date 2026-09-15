package com.acme;

import com.acme.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //encoding
    public static String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    //check password match
    public boolean verifyPassword(String rawPassword, String storedHash) {
        return encoder.matches(rawPassword, storedHash);
    }

    public static boolean changePassword(String newPass,Users user) throws IOException {
        user.setPassword(hashPassword(newPass));
        return true;
    }
    private List<Users> users = new ArrayList<>();

    public void initialUsers(){
        System.out.println("initializing users");
        addUser(new Banker("admin",hashPassword("admin123"),"Adam"));
        addUser(new Customer("Moham",hashPassword("moham123"),"Mohammed"));
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

            //match creds
            boolean isValidCredentials = lineStream
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 3) // Prevent crashes on empty lines
                    .filter(parts -> parts[1].equalsIgnoreCase(userName)) // Match username
                    .anyMatch(parts -> verifyPassword(password, parts[2])); // Verify secure hash

            // return user object
            if (isValidCredentials) {
                return users.stream()
                        .filter(user -> user.getUserName().equalsIgnoreCase(userName))
                        .findFirst()
                        .orElse(null);
            }

            return null; // Login failed (wrong username or password)

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
}