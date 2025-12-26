package com.aditya;

import java.util.UUID;
import javax.servlet.http.HttpSession;

public class CSRFTokenUtil {
    public static final String CSRF_SESSION_KEY = "CSRF_TOKEN";

    public static String getOrCreateToken(HttpSession session) {
        Object t = session.getAttribute(CSRF_SESSION_KEY);
        if (t instanceof String) return (String) t;
        String token = UUID.randomUUID().toString();
        session.setAttribute(CSRF_SESSION_KEY, token);
        return token;
    }

    public static boolean validateToken(HttpSession session, String token) {
        if (token == null) return false;
        Object t = session.getAttribute(CSRF_SESSION_KEY);
        return t != null && token.equals(t.toString());
    }
}
