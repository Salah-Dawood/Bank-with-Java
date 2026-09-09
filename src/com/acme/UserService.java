package com.acme;

import com.acme.Users;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<Users> users = new ArrayList<>();

    public void initialUsers(){
        System.out.println("initializing users");
        addUser(new Banker("admin","admin123","Adam"));
        addUser(new Customer("Moham","moham123","Mohammed"));
    }
    public void addUser(Users user) {
        users.add(user);
        try (FileWriter fw = new FileWriter(FileDBConfig.usersFile.toString(), true);
             BufferedWriter writer = new BufferedWriter(fw)) {

            writer.write(user.toString());
            writer.newLine();
            System.out.println("Successfully appended user: " + user);
        }catch (
                IOException e) {
            System.err.println("Failed to write user to database: " + e.getMessage());
        }
    }

    public Users login(String username, String password) {
        for (Users user : users) {
            if (user.getUserName().equals(username) && user.verifyPassword(password)) return user;
        }

        return null;
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

}