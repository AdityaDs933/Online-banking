package com.aditya;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class AccountIntegrationTest {

    private static DBConnection testDB;

    @BeforeAll
    public static void setupDb() throws Exception {
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        testDB = new DBConnection(url, "org.h2.Driver");
        try (Connection c = testDB.openConn(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE Account (Username VARCHAR(50) PRIMARY KEY, Password VARCHAR(255), Name VARCHAR(100))");
        }
    }

    @AfterAll
    public static void tearDown() {
        DBConnection.shutdownPool();
    }

    @Test
    public void testSignUpAndSignInAndChangePassword() throws Exception {
        DBConnection db = testDB;
        Account acct = new Account("testuser", "PasswordA1", "PasswordA1", "Test User", db);
        boolean signedUp = acct.signUp();
        assertTrue(signedUp, "signUp should succeed");

        Account loginAcct = new Account("testuser", "PasswordA1", db);
        String name = loginAcct.signIn();
        assertEquals("Test User", name);

        boolean changed = loginAcct.changePassword("NewPassB2");
        assertTrue(changed, "changePassword should succeed");

        Account reLogin = new Account("testuser", "NewPassB2", db);
        String newName = reLogin.signIn();
        assertEquals("Test User", newName);
    }
}
