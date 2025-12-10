package com.companyz.ems.ui;

import com.companyz.ems.auth.AuthService;
import com.companyz.ems.dao.ConnectionManager;
import com.companyz.ems.dao.EmployeeDAO;
import com.companyz.ems.dao.PayrollDAO;
import com.companyz.ems.security.SessionManager;
import com.companyz.ems.service.EmployeeService;
import com.companyz.ems.service.PayrollService;

public class Main {

    public static void main(String[] args) {
        ConnectionManager cm = new ConnectionManager(
                "jdbc:mysql://localhost:3306/employeedata2?useSSL=false&serverTimezone=UTC",
                "root",
                "H1rrison1029!"
        );

        SessionManager sessionManager = new SessionManager();
        AuthService authService = new AuthService(cm, sessionManager);

        // Initialize GUI with services
        new LoginGUI(authService, cm);
    }
}

