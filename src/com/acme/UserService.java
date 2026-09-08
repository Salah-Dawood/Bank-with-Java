package com.acme;

import com.acme.Users;

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
        System.out.println("addind " + user);
        users.add(user);
    }

    public Users login(String username, String password) {
        System.out.println("WOW LOGIN GOT CALLED");
        System.out.println(users);
        for (Users user : users) {
            System.out.println(user + " " + user.getPassword());
            if (user.getUserName().equals(username) && user.verifyPassword(password)) {

                return user;
            }
        }

        return null;
    }
}