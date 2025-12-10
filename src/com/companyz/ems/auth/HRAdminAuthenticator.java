package com.companyz.ems.auth;

import com.companyz.ems.dao.ConnectionManager;
import com.companyz.ems.security.UserRole;
import com.companyz.ems.user.HREmployee;
import java.sql.*;

public class HRAdminAuthenticator implements Authenticator {

    private final ConnectionManager cm;
    private HREmployee currentUser;

    public HRAdminAuthenticator(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public boolean login(String username, String password) {
        String sql = """
            SELECT user_id, username, password_hash, access_level, department
            FROM users
            WHERE username = ? AND role = 'HR_ADMIN'
            """;
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                String pw = rs.getString("password_hash");
                if (!pw.equals(password)) return false;

                int id = rs.getInt("user_id");
                String access = rs.getString("access_level");
                String dept = rs.getString("department");
                currentUser = new HREmployee(id, username, pw, access, dept);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Login failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public UserRole getRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    @Override
    public void logout() {
        currentUser = null;
    }

    public HREmployee getCurrentUser() {
        return currentUser;
    }
}

