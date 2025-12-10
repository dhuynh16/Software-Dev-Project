package com.companyz.ems.user;

import com.companyz.ems.security.UserRole;

public class HREmployee extends User {

    private final String accessLevel;
    private final String department;

    // 👇 THIS is the constructor HRAdminAuthenticator expects
    public HREmployee(int userId,
                      String username,
                      String passwordHash,
                      String accessLevel,
                      String department) {
        super(userId, username, passwordHash, UserRole.HR_ADMIN);
        this.accessLevel = accessLevel;
        this.department = department;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getDepartment() {
        return department;
    }

    public void manageEmployees() {
        // placeholder for admin-specific actions
    }
}
