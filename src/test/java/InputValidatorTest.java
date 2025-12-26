package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Online_banking.InputValidator;

public class InputValidatorTest {
    @Test
    public void testIsValidEmail() {
        assertTrue(InputValidator.isValidEmail("test@example.com"));
        assertFalse(InputValidator.isValidEmail("invalid-email"));
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(InputValidator.isValidPassword("StrongPass123!"));
        assertFalse(InputValidator.isValidPassword("123"));
    }
}
