package com.acme;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileDBConfig {
    public static final Path rootDir = Paths.get("DB");
    public static final Path usersFile = rootDir.resolve("users.txt");
}
