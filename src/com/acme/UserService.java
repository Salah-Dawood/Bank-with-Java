package com.acme;

import com.acme.Users;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
}