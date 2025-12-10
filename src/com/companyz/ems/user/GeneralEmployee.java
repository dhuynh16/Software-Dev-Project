package com.companyz.ems.user;

import com.companyz.ems.security.UserRole;

public class GeneralEmployee extends User {

    public GeneralEmployee(int id, String username, String passwordHash) {
        this.userId = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = UserRole.GENERAL_EMPLOYEE;
    }
}
