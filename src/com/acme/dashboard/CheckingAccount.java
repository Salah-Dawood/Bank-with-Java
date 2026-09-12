package com.acme.dashboard;

import com.acme.FileService;
import com.acme.Session;
import com.acme.Users;

import java.io.IOException;

public class CheckingAccount extends Account{

    public CheckingAccount() throws IOException {
        super("Checking");
    }
}
