package com.companyz.ems.auth;

import com.companyz.ems.security.UserRole;

public interface Authenticator {
    boolean login(String username, String password);

    UserRole getRole();

    void logout();
}