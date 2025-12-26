package com.aditya;

import org.apache.commons.text.StringEscapeUtils;

public class InputValidator {

    // Escape HTML to prevent XSS in JSPs
    public static String escapeHtml(String input) {
        if (input == null) return "";
        return StringEscapeUtils.escapeHtml4(input);
    }

    public static boolean isValidUsername(String u) {
        if (u == null) return false;
        return u.matches("[A-Za-z0-9_\\-]{3,30}");
    }

    public static boolean isValidAmount(String a) {
        if (a == null) return false;
        try {
            double v = Double.parseDouble(a);
            return v >= 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPassword(String p) {
        if (p == null) return false;
        // At least 8 chars, at least one number and one letter
        return p.length() >= 8 && p.matches("(?=.*[A-Za-z])(?=.*\\d).+");
    }
}
