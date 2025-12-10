package com.companyz.ems.ui;

import com.companyz.ems.domain.Employee;
import com.companyz.ems.security.Session;
import com.companyz.ems.service.EmployeeService;
import com.companyz.ems.service.PayrollService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class HRAdminGUI extends JFrame {
    private Session session;
    private EmployeeService employeeService;
    private PayrollService payrollService;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public HRAdminGUI(Session session, EmployeeService employeeService, PayrollService payrollService) {
        this.session = session;
        this.employeeService = employeeService;
        this.payrollService = payrollService;

        setTitle("Employee Management System - HR Admin");
        setSize(900, 600);
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
        JLabel headerLabel = new JLabel("HR Admin Dashboard - " + session.getUser().getUsername());
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Center panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Employees Tab
        JPanel employeesPanel = createEmployeesPanel();
        tabbedPane.addTab("Employees", employeesPanel);

        // Payroll Tab
        JPanel payrollPanel = createPayrollPanel();
        tabbedPane.addTab("Payroll", payrollPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createEmployeesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton viewAllButton = new JButton("View All Employees");
        viewAllButton.addActionListener(e -> loadEmployeesTable());

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> openAddEmployeeDialog());

        JButton updateButton = new JButton("Update Employee");
        updateButton.addActionListener(e -> openUpdateEmployeeDialog());

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(e -> deleteEmployee());

        buttonPanel.add(viewAllButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Employee ID");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Job Title");
        tableModel.addColumn("Salary");

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPayrollPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton generateButton = new JButton("Generate Pay Statement");
        generateButton.addActionListener(e -> openPayStatementDialog());

        JButton viewButton = new JButton("View Statements");
        viewButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "View Statements feature coming soon"));

        buttonPanel.add(generateButton);
        buttonPanel.add(viewButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        JLabel infoLabel = new JLabel("Select an option to manage payroll.");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(infoLabel, BorderLayout.CENTER);

        return panel;
    }

    private void loadEmployeesTable() {
        tableModel.setRowCount(0);
        try {
            List<Employee> employees = employeeService.searchForAdmin(session, null, null, null, null);
            for (Employee emp : employees) {
                tableModel.addRow(new Object[]{
                        emp.getEmpid(),
                        emp.getFullName().split(" ")[0],
                        emp.getFullName().split(" ").length > 1 ? emp.getFullName().split(" ")[1] : "",
                        emp.getEmail(),
                        emp.getJobTitle(),
                        emp.getSalary()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage());
        }
    }

    private void openAddEmployeeDialog() {
        JOptionPane.showMessageDialog(this, "Add Employee feature coming soon");
    }

    private void openUpdateEmployeeDialog() {
        if (employeeTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update.");
            return;
        }
        JOptionPane.showMessageDialog(this, "Update Employee feature coming soon");
    }

    private void deleteEmployee() {
        if (employeeTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.");
            return;
        }
        JOptionPane.showMessageDialog(this, "Delete Employee feature coming soon");
    }

    private void openPayStatementDialog() {
        JOptionPane.showMessageDialog(this, "Generate Pay Statement feature coming soon");
    }

    private void logout() {
        dispose();
        System.exit(0);  // Exit the application
    }
}
