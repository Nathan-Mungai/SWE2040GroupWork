package util;

import models.LoginResponse;

public class SessionManager {
    private static LoginResponse currentUser;

    public static void setCurrentUser(LoginResponse user) {
        currentUser = user;
    }

    public static LoginResponse getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void clearSession() {
        currentUser = null;
    }
}