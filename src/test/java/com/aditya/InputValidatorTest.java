package com.aditya;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {

    @Test
    public void validEmail() {
        assertTrue(InputValidator.isValidEmail("test@example.com"));
        assertFalse(InputValidator.isValidEmail("invalid-email"));
    }

    @Test
    public void validUsernameAndPassword() {
        assertTrue(InputValidator.isValidUsername("user_123"));
        assertFalse(InputValidator.isValidUsername("a"));
        assertTrue(InputValidator.isValidPassword("Aa12345"));
        assertFalse(InputValidator.isValidPassword("short"));
    }
}
