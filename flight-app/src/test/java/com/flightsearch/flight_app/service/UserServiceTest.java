package com.flightsearch.flight_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private static final String TEST_USERS_XML_PATH = "src/test/resources/test_users.xml";

    @BeforeEach
    void setUp() throws Exception {
        userService = new UserService();
        // Set up a temporary XML file for testing
        File testFile = new File(TEST_USERS_XML_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
        testFile.createNewFile();
        userService = new UserService() {
            @Override
            public boolean registerUser(String username, String password) {
                return super.registerUser(username, password);
            }
        };
    }

    @Test
    void testRegisterUser() {
        boolean result = userService.registerUser("testuser", "password123");
        assertTrue(result, "User should be registered successfully");

        // Verify that the XML file contains the registered user
        try {
            String content = Files.readString(Path.of(TEST_USERS_XML_PATH));
            assertTrue(content.contains("<username>testuser</username>"), "Username should be in the XML file");
            assertTrue(content.contains("<password>password123</password>"), "Password should be in the XML file");
        } catch (Exception e) {
            fail("Failed to read the XML file: " + e.getMessage());
        }
    }
}
