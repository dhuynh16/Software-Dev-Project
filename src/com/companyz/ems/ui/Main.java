package com.companyz.ems.ui;

import com.companyz.ems.auth.AuthService;
import com.companyz.ems.dao.ConnectionManager;
import com.companyz.ems.dao.EmployeeDAO;
import com.companyz.ems.dao.PayrollDAO;
import com.companyz.ems.security.SessionManager;
import com.companyz.ems.service.EmployeeService;
import com.companyz.ems.service.PayrollService;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // TODO: adjust URL / user / password if needed
            ConnectionManager cm = new ConnectionManager(
                    "jdbc:mysql://localhost:3306/employeedata2?useSSL=false&serverTimezone=UTC",
                    "root",
                    "SHAD8241!hand"
            );

            SessionManager sessionManager = new SessionManager();
            AuthService authService = new AuthService(cm, sessionManager);
            EmployeeDAO employeeDAO = new EmployeeDAO(cm);
            PayrollDAO payrollDAO = new PayrollDAO(cm);
            EmployeeService employeeService = new EmployeeService(employeeDAO);
            PayrollService payrollService = new PayrollService(payrollDAO);

            new EmployeeManagementGUI(authService, employeeService, payrollService);
        });
    }
}
