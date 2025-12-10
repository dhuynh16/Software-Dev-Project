package com.companyz.ems.ui;

import com.companyz.ems.auth.AuthService;
import com.companyz.ems.dao.ConnectionManager;
import com.companyz.ems.dao.EmployeeDAO;
import com.companyz.ems.dao.PayrollDAO;
import com.companyz.ems.domain.Employee;
import com.companyz.ems.domain.PayStatement;
import com.companyz.ems.security.Session;
import com.companyz.ems.security.SessionManager;
import com.companyz.ems.security.UserRole;
import com.companyz.ems.service.EmployeeService;
import com.companyz.ems.service.PayrollService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class EmployeeManagementGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private AuthService authService;
    private EmployeeService employeeService;
    private PayrollService payrollService;
    private Session currentSession;
    
    public EmployeeManagementGUI(AuthService authService, EmployeeService employeeService, PayrollService payrollService) {
        this.authService = authService;
        this.employeeService = employeeService;
        this.payrollService = payrollService;
        
        setTitle("Employee Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create different panels
        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createHRMenuPanel(), "hrMenu");
        mainPanel.add(createEmployeeMenuPanel(), "employeeMenu");
        mainPanel.add(createSearchEmployeePanel(), "searchEmployee");
        mainPanel.add(createAddEmployeePanel(), "addEmployee");
        mainPanel.add(createViewPayStatementsPanel(), "viewPayStatements");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        
        setVisible(true);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username
        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(userLabel, gbc);
        
        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(userField, gbc);
        
        // Password
        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);
        
        JPasswordField passField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passField, gbc);
        
        // Login Button
        JButton loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(loginButton, gbc);
        
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            Session session = authService.login(username, password);
            if (session != null) {
                currentSession = session;
                if (session.getRole() == UserRole.HR_ADMIN) {
                    cardLayout.show(mainPanel, "hrMenu");
                } else {
                    cardLayout.show(mainPanel, "employeeMenu");
                }
                userField.setText("");
                passField.setText("");
            } else {
                JOptionPane.showMessageDialog(panel, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
    
    private JPanel createHRMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        // Title
        JLabel titleLabel = new JLabel("HR Admin Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton searchBtn = new JButton("Search Employee");
        JButton addBtn = new JButton("Add Employee");
        JButton updateSalaryBtn = new JButton("Update Salary Range");
        JButton createPayBtn = new JButton("Create Pay Statement");
        JButton logoutBtn = new JButton("Logout");
        
        searchBtn.addActionListener(e -> cardLayout.show(mainPanel, "searchEmployee"));
        addBtn.addActionListener(e -> cardLayout.show(mainPanel, "addEmployee"));
        logoutBtn.addActionListener(e -> {
            currentSession = null;
            cardLayout.show(mainPanel, "login");
        });
        
        buttonPanel.add(searchBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(updateSalaryBtn);
        buttonPanel.add(createPayBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEmployeeMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        // Title
        JLabel titleLabel = new JLabel("Employee Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton viewPayBtn = new JButton("View Pay Statements");
        JButton logoutBtn = new JButton("Logout");
        
        viewPayBtn.addActionListener(e -> cardLayout.show(mainPanel, "viewPayStatements"));
        logoutBtn.addActionListener(e -> {
            currentSession = null;
            cardLayout.show(mainPanel, "login");
        });
        
        buttonPanel.add(viewPayBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSearchEmployeePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        // Top Panel with search
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(new Color(240, 240, 240));
        
        JLabel searchLabel = new JLabel("Search by ID:");
        JTextField searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        JButton backBtn = new JButton("Back");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(backBtn);
        
        // Results table
        JTable resultsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        searchBtn.addActionListener(e -> {
            try {
                int empId = Integer.parseInt(searchField.getText());
                List<Employee> results = employeeService.searchForAdmin(currentSession, null, null, null, empId);
                
                if (!results.isEmpty()) {
                    String[] columns = {"ID", "First Name", "Last Name", "Email", "Division", "Salary"};
                    Object[][] data = new Object[results.size()][6];
                    
                    for (int i = 0; i < results.size(); i++) {
                        Employee emp = results.get(i);
                        data[i][0] = emp.getEmpid();
                        data[i][1] = emp.getFname();
                        data[i][2] = emp.getLname();
                        data[i][3] = emp.getEmail();
                        data[i][4] = emp.getDivision();
                        data[i][5] = emp.getSalary();
                    }
                    
                    DefaultTableModel model = new DefaultTableModel(data, columns);
                    resultsTable.setModel(model);
                } else {
                    JOptionPane.showMessageDialog(panel, "Employee not found", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid ID format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "hrMenu"));
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAddEmployeePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JTextField firstNameField = createFormField(formPanel, "First Name:", 0, gbc);
        JTextField lastNameField = createFormField(formPanel, "Last Name:", 1, gbc);
        JTextField emailField = createFormField(formPanel, "Email:", 2, gbc);
        JTextField departmentField = createFormField(formPanel, "Department:", 3, gbc);
        JTextField salaryField = createFormField(formPanel, "Salary:", 4, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton addBtn = new JButton("Add Employee");
        JButton backBtn = new JButton("Back");
        
        addBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, "Add Employee feature requires additional form fields.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "hrMenu"));
        
        buttonPanel.add(addBtn);
        buttonPanel.add(backBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createViewPayStatementsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");
        topPanel.add(refreshBtn);
        topPanel.add(backBtn);
        
        // Results table
        JTable payTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(payTable);
        
        refreshBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, "Pay history feature requires integration with PayrollService.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "employeeMenu"));
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JTextField createFormField(JPanel panel, String label, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        
        JTextField field = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(field, gbc);
        
        return field;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
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
