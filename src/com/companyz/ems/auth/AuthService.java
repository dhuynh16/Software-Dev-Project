package com.companyz.ems.auth;

import com.companyz.ems.dao.ConnectionManager;
import com.companyz.ems.security.Session;
import com.companyz.ems.security.SessionManager;
import com.companyz.ems.security.UserRole;
import com.companyz.ems.user.User;

import java.sql.*;

public class AuthService {

    private final ConnectionManager cm;
    private final SessionManager sessionManager;

    public AuthService(ConnectionManager cm, SessionManager sessionManager) {
        this.cm = cm;
        this.sessionManager = sessionManager;
    }

    public Session login(String username, String password) {
        String sql = """
            SELECT user_id, username, password_hash, role, empid
            FROM users
            WHERE username = ?
            """;

        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String storedHash = rs.getString("password_hash");
                if (!storedHash.equals(password)) return null;

                int userId = rs.getInt("user_id");
                UserRole role = UserRole.valueOf(rs.getString("role"));
                Integer empid = rs.getObject("empid") == null ?
                        null : rs.getInt("empid");

                User user = new User(userId, username, storedHash, role, empid);
                return sessionManager.createSession(user);
            }
        } catch (SQLException e) {
            System.err.println("Login failed: " + e.getMessage());
            return null;
        }
    }
}
