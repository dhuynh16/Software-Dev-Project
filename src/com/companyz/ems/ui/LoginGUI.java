package com.companyz.ems.ui;

import com.companyz.ems.auth.AuthService;
import com.companyz.ems.dao.ConnectionManager;
import com.companyz.ems.dao.EmployeeDAO;
import com.companyz.ems.dao.PayrollDAO;
import com.companyz.ems.security.Session;
import com.companyz.ems.security.SessionManager;
import com.companyz.ems.security.UserRole;
import com.companyz.ems.service.EmployeeService;
import com.companyz.ems.service.PayrollService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;
    private AuthService authService;
    private ConnectionManager cm;

    public LoginGUI(AuthService authService, ConnectionManager cm) {
        this.authService = authService;
        this.cm = cm;

        setTitle("Employee Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel titleLabel = new JLabel("EMS System Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));

        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        errorLabel.setForeground(Color.RED);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(errorLabel);
        formPanel.add(new JLabel()); // Empty cell for spacing

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.addActionListener(new LoginButtonListener());

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setPreferredSize(new Dimension(100, 35));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter username and password.");
                return;
            }

            Session session = authService.login(username, password);

            if (session == null) {
                errorLabel.setText("Invalid username or password.");
                passwordField.setText("");
                return;
            }

            // Close login window and open appropriate menu
            dispose();

            EmployeeDAO employeeDAO = new EmployeeDAO(cm);
            PayrollDAO payrollDAO = new PayrollDAO(cm);
            EmployeeService employeeService = new EmployeeService(employeeDAO);
            PayrollService payrollService = new PayrollService(payrollDAO);

            if (session.getRole() == UserRole.HR_ADMIN) {
                new HRAdminGUI(session, employeeService, payrollService);
            } else if (session.getRole() == UserRole.GENERAL_EMPLOYEE) {
                new EmployeeGUI(session, employeeService);
            }
        }
    }
}
