package com.companyz.ems.ui;

import com.companyz.ems.domain.Employee;
import com.companyz.ems.security.Session;
import com.companyz.ems.service.EmployeeService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployeeGUI extends JFrame {
    private Session session;
    private EmployeeService employeeService;
    private JTextArea infoArea;

    public EmployeeGUI(Session session, EmployeeService employeeService) {
        this.session = session;
        this.employeeService = employeeService;

        setTitle("Employee Management System - Employee");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel headerLabel = new JLabel("Employee Dashboard - " + session.getUser().getUsername());
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Center panel with options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewMyInfoButton = new JButton("View My Information");
        viewMyInfoButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewMyInfoButton.setPreferredSize(new Dimension(200, 50));
        viewMyInfoButton.addActionListener(e -> viewMyInfo());

        JButton viewPayStatementButton = new JButton("View Pay Statements");
        viewPayStatementButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewPayStatementButton.setPreferredSize(new Dimension(200, 50));
        viewPayStatementButton.addActionListener(e -> viewPayStatements());

        optionsPanel.add(viewMyInfoButton);
        optionsPanel.add(viewPayStatementButton);

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // Info area
        infoArea = new JTextArea();
        infoArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setVisible(false);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void viewMyInfo() {
        try {
            Integer empid = session.getUser().getEmpid();
            List<Employee> results = employeeService.searchForEmployee(session, null, null, null, empid);

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No employee record found.");
                return;
            }

            Employee emp = results.get(0);
            StringBuilder info = new StringBuilder();
            info.append("=== Your Information ===\n\n");
            info.append("Employee ID: ").append(emp.getEmpid()).append("\n");
            info.append("Name: ").append(emp.getFullName()).append("\n");
            info.append("Email: ").append(emp.getEmail()).append("\n");
            info.append("Date of Birth: ").append(emp.getDob()).append("\n");
            info.append("Hire Date: ").append(emp.getHireDate()).append("\n");
            info.append("Job Title: ").append(emp.getJobTitle()).append("\n");
            info.append("Salary: $").append(String.format("%.2f", emp.getSalary())).append("\n");
            info.append("Division: ").append(emp.getDivision()).append("\n");
            info.append("Status: ").append(emp.getStatus()).append("\n");

            infoArea.setText(info.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error retrieving your information: " + e.getMessage());
        }
    }

    private void viewPayStatements() {
        JOptionPane.showMessageDialog(this, "View Pay Statements feature coming soon");
    }

    private void logout() {
        dispose();
        System.exit(0);  // Exit the application
    }
}
