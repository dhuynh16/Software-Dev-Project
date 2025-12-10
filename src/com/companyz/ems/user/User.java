package com.companyz.ems.user;

import com.companyz.ems.security.UserRole;

public class User {
    private final int userId;
    private final String username;
    private final String passwordHash;
    private final UserRole role;
    private final Integer empid; // null for HR

    public User(int userId, String username, String passwordHash,
                UserRole role, Integer empid) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.empid = empid;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
    public Integer getEmpid() { return empid; }

    public boolean passwordMatches(String rawPassword) {
        // plain-text for class project
        return passwordHash.equals(rawPassword);
    }
}



