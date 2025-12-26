/******************************************************************************
*	Online Banking System - Input Validation Utility Class
*	Date: December, 2025
*******************************************************************************/

package com.aditya;

/**
 * Utility class for input validation to improve code quality and security.
 * Demonstrates best practices for parameter validation.
 */
public class InputValidator {
    
    /**
     * Validates that a string is not null or empty.
     * @param input the input string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Validates username format.
     * @param username the username to validate
     * @return true if valid (alphanumeric, 3-20 chars)
     */
    public static boolean isValidUsername(String username) {
        if (!isValidString(username)) return false;
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    /**
     * Validates password strength.
     * @param password the password to validate
     * @return true if valid (at least 6 chars, mixed case/digits)
     */
    public static boolean isValidPassword(String password) {
        if (!isValidString(password)) return false;
        return password.length() >= 6 && password.matches(".*[a-z].*") && 
               password.matches(".*[A-Z].*");
    }

    /**
     * Validates email format.
     * @param email the email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (!isValidString(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Sanitizes SQL input to prevent basic SQL injection.
     * Note: Use prepared statements instead for production code.
     * @param input the input to sanitize
     * @return sanitized string
     */
    public static String sanitizeSQLInput(String input) {
        if (input == null) return "";
        return input.replaceAll("'", "''").replaceAll("\"", "\\\\\"");
    }

    /**
     * Validates account number format.
     * @param accountNumber the account number to validate
     * @return true if valid
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (!isValidString(accountNumber)) return false;
        return accountNumber.matches("^[A-Z0-9]{10,15}$");
    }

    /**
     * Validates amount is positive.
     * @param amount the amount to validate
     * @return true if valid
     */
    public static boolean isValidAmount(String amount) {
        try {
            float value = Float.parseFloat(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
