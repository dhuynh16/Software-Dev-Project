package com.companyz.ems.auth;

import com.companyz.ems.dao.DBConnection;
import com.companyz.ems.security.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeAuthenticator implements Authenticator {

    private UserRole role;

    @Override
    public boolean login(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT empid FROM employees WHERE email = ? AND password = ? AND role = 'GENERAL_EMPLOYEE'";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                role = UserRole.GENERAL_EMPLOYEE;
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error during employee login: " + e.getMessage());
        }

        return false;
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public void logout() {
        role = null;
    }
}
