package com.identityplatform.authentication;

import com.identityplatform.user.User;

public class SecurityContext {

    public static final String ATTRIBUTE_NAME = "com.identityplatform.authentication.SecurityContext";
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return user != null;
    }
}