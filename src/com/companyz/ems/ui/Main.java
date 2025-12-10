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
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // TODO: adjust URL / user / password if needed
        ConnectionManager cm = new ConnectionManager(
                "jdbc:mysql://localhost:3306/employeedata2?useSSL=false&serverTimezone=UTC",
                "root",
                "H1rrison1029!"
        );

        SessionManager sessionManager = new SessionManager();
        AuthService authService = new AuthService(cm, sessionManager);
        EmployeeDAO employeeDAO = new EmployeeDAO(cm);
        PayrollDAO payrollDAO = new PayrollDAO(cm);
        EmployeeService employeeService = new EmployeeService(employeeDAO);
        PayrollService payrollService = new PayrollService(payrollDAO);

        System.out.println("=== EMS System ===");
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        Session session = authService.login(user, pass);
        if (session == null) {
            System.out.println("Invalid login.");
            return;
        }

        if (session.getRole() == UserRole.HR_ADMIN) {
            hrMenu(session, employeeService, payrollService);
        } else {
            employeeMenu(session, employeeService, payrollService);
        }

        sc.close();
    }

    // ===================== HR ADMIN MENU =========================

    private static void hrMenu(Session session,
                               EmployeeService employeeService,
                               PayrollService payrollService) {
        while (true) {
            System.out.println("\n--- HR Admin Menu ---");
            System.out.println("1) Search employees (for editing)");
            System.out.println("2) Update an employee");
            System.out.println("3) Increase salaries in range");
            System.out.println("4) Total pay by job title (month)");
            System.out.println("5) Total pay by division (month)");
            System.out.println("6) Employees hired within date range");
            System.out.println("7) Create New Employee");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> hrSearchEmployees(session, employeeService);
                    case 2 -> hrUpdateEmployee(session, employeeService);
                    case 3 -> hrIncreaseSalaries(session, employeeService);
                    case 4 -> hrTotalPayByJobTitle(session, payrollService);
                    case 5 -> hrTotalPayByDivision(session, payrollService);
                    case 6 -> hrEmployeesHiredBetween(session, employeeService);
                    case 7 -> createEmployeeMenu(session, employeeService);
                    case 0 -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void hrSearchEmployees(Session session, EmployeeService employeeService) {
        System.out.print("Name (or blank): ");
        String name = sc.nextLine();
        System.out.print("DOB (YYYY-MM-DD or blank): ");
        String dobStr = sc.nextLine();
        System.out.print("SSN (or blank): ");
        String ssn = sc.nextLine();
        System.out.print("EmpID (or blank): ");
        String empidStr = sc.nextLine();

        LocalDate dob = dobStr.isBlank() ? null : LocalDate.parse(dobStr);
        Integer empid = empidStr.isBlank() ? null : Integer.parseInt(empidStr);

        List<Employee> results = employeeService.searchForAdmin(session, name, dob, ssn, empid);
        if (results.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        for (Employee e : results) {
            System.out.println(
                    "EmpID: " + e.getEmpid() +
                    " | Name: " + e.getFullName() +
                    " | DOB: " + e.getDob() +
                    " | SSN: " + e.getSsn() +
                    " | Title: " + e.getJobTitle() +
                    " | Division: " + e.getDivision() +
                    " | Status: " + e.getStatus() +
                    " | Salary: $" + e.getSalary()
            );
        }
    }

    private static void hrUpdateEmployee(Session session, EmployeeService employeeService) {
        System.out.print("EmpID to update: ");
        int empid = Integer.parseInt(sc.nextLine());

        List<Employee> list = employeeService.searchForAdmin(session, null, null, null, empid);
        if (list.isEmpty()) {
            System.out.println("No employee found with that ID.");
            return;
        }

        Employee e = list.get(0);

        System.out.println("Current email: " + e.getEmail());
        System.out.print("New email (blank to keep): ");
        String email = sc.nextLine();
        if (!email.isBlank()) e.setEmail(email);

        System.out.println("Current division: " + e.getDivision());
        System.out.print("New division (blank to keep): ");
        String div = sc.nextLine();
        if (!div.isBlank()) e.setDivision(div);

        System.out.println("Current job title: " + e.getJobTitle());
        System.out.print("New job title (blank to keep): ");
        String jt = sc.nextLine();
        if (!jt.isBlank()) e.setJobTitle(jt);

        System.out.println("Current status: " + e.getStatus());
        System.out.print("New status (blank to keep): ");
        String st = sc.nextLine();
        if (!st.isBlank()) e.setStatus(st);

        System.out.println("Current salary: " + e.getSalary());
        System.out.print("New salary (blank to keep): ");
        String salStr = sc.nextLine();
        if (!salStr.isBlank()) e.setSalary(Double.parseDouble(salStr));

        employeeService.updateEmployee(session, e);
        System.out.println("Employee updated.");
    }

    private static void hrIncreaseSalaries(Session session, EmployeeService employeeService) {
        System.out.print("Min salary: ");
        double min = Double.parseDouble(sc.nextLine());
        System.out.print("Max salary: ");
        double max = Double.parseDouble(sc.nextLine());
        System.out.print("Percent increase (e.g., 3.2): ");
        double pct = Double.parseDouble(sc.nextLine());

        int updated = employeeService.updateSalaryRange(session, min, max, pct);
        System.out.println("Updated " + updated + " employees.");
    }

    private static void hrTotalPayByJobTitle(Session session,
                                         PayrollService payrollService) {
    System.out.print("Year (YYYY): ");
    int year = Integer.parseInt(sc.nextLine());

    System.out.print("Month (1-12): ");
    int month = Integer.parseInt(sc.nextLine());

    List<String> rows = payrollService.totalPayByJobTitle(session, year, month);

    if (rows.isEmpty()) {
        System.out.println("No payroll records found for that period.");
        return;
    }

    System.out.println();
    System.out.println("=== Total Pay by Job Title for " +
            year + "-" + String.format("%02d", month) + " ===");
    System.out.println();
    System.out.printf("%-30s %-15s%n", "Job Title", "Total Pay");
    System.out.println("-----------------------------------------------");

    for (String row : rows) {
        // row format: "Job Title|12345.67"
        String[] parts = row.split("\\|");
        String jobTitle = parts[0].trim();
        double total = Double.parseDouble(parts[1].trim());

        System.out.printf("%-30s $%,.2f%n", jobTitle, total);
    }
}


    private static void hrTotalPayByDivision(Session session, PayrollService payrollService) {
        System.out.print("Year (YYYY): ");
        int year = Integer.parseInt(sc.nextLine());
        System.out.print("Month (1-12): ");
        int month = Integer.parseInt(sc.nextLine());

        List<String> rows = payrollService.totalPayByDivision(session, year, month);
        if (rows.isEmpty()) {
            System.out.println("No data for that period.");
        } else {
            rows.forEach(System.out::println);
        }
    }

    private static void hrEmployeesHiredBetween(Session session, EmployeeService employeeService) {
        System.out.print("From date (YYYY-MM-DD): ");
        LocalDate from = LocalDate.parse(sc.nextLine());
        System.out.print("To date (YYYY-MM-DD): ");
        LocalDate to = LocalDate.parse(sc.nextLine());

        List<Employee> hired = employeeService.employeesHiredBetween(session, from, to);
        if (hired.isEmpty()) {
            System.out.println("No employees hired in that range.");
        } else {
            for (Employee e : hired) {
                System.out.println(
                        e.getEmpid() + " - " + e.getFullName() +
                        " | Hired: " + e.getHireDate() +
                        " | Division: " + e.getDivision() +
                        " | Title: " + e.getJobTitle()
                );
            }
        }
    }

    private static void createEmployeeMenu(Session session,
                                           EmployeeService employeeService) {
        System.out.println("\n=== Create New Employee ===");

        try {
            System.out.print("Enter Employee ID: ");
            int empid = Integer.parseInt(sc.nextLine());

            System.out.print("First Name: ");
            String fname = sc.nextLine();

            System.out.print("Last Name: ");
            String lname = sc.nextLine();

            System.out.print("DOB (YYYY-MM-DD or blank): ");
            String dobInput = sc.nextLine();
            LocalDate dob = dobInput.isBlank() ? null : LocalDate.parse(dobInput);

            System.out.print("SSN (XXX-XX-XXXX): ");
            String ssn = sc.nextLine();

            System.out.print("Hire Date (YYYY-MM-DD or blank): ");
            String hireInput = sc.nextLine();
            LocalDate hireDate = hireInput.isBlank() ? null : LocalDate.parse(hireInput);

            System.out.print("Base Salary: ");
            double salary = Double.parseDouble(sc.nextLine());

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Division: ");
            String division = sc.nextLine();

            System.out.print("Job Title: ");
            String jobTitle = sc.nextLine();

            System.out.print("Status (ACTIVE/INACTIVE): ");
            String status = sc.nextLine();

            Employee e = new Employee(
                    empid, fname, lname, dob, ssn, hireDate,
                    salary, email, division, jobTitle, status
            );

            employeeService.createEmployee(session, e);
            System.out.println("Employee created successfully!");
        } catch (Exception ex) {
            System.out.println("Error creating employee: " + ex.getMessage());
        }
    }

    // ===================== GENERAL EMPLOYEE MENU =========================

    private static void employeeMenu(Session session,
                                     EmployeeService employeeService,
                                     PayrollService payrollService) {
        while (true) {
            System.out.println("\n--- Employee Menu ---");
            System.out.println("1) Search my info");
            System.out.println("2) View my pay history");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> empSearchOwnInfo(session, employeeService);
                    case 2 -> empViewPayHistory(session, payrollService);
                    case 0 -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void empSearchOwnInfo(Session session,
                                     EmployeeService employeeService) {
    System.out.print("Name (or blank): ");
    String name = sc.nextLine();
    System.out.print("DOB (YYYY-MM-DD or blank): ");
    String dobStr = sc.nextLine();
    System.out.print("SSN (or blank): ");
    String ssn = sc.nextLine();
    System.out.print("EmpID (or blank): ");
    String empidStr = sc.nextLine();

    LocalDate dob = dobStr.isBlank() ? null : LocalDate.parse(dobStr);
    Integer empid = empidStr.isBlank() ? null : Integer.parseInt(empidStr);

    List<Employee> results = employeeService.searchForEmployee(session, name, dob, ssn, empid);

    if (results.isEmpty()) {
        System.out.println("No matching records.");
        return;
    }

    for (Employee e : results) {
        System.out.println(
                "EmpID: " + e.getEmpid() +
                " | Name: " + e.getFullName() +
                " | DOB: " + e.getDob() +
                " | Division: " + e.getDivision() +
                " | Title: " + e.getJobTitle() +
                " | Status: " + e.getStatus()
                // 👈 NO salary, NO SSN here for general employees
        );
    }
}


    private static void empViewPayHistory(Session session,
                                          PayrollService payrollService) {
        List<PayStatement> history = payrollService.ownPayHistory(session);
        if (history.isEmpty()) {
            System.out.println("No pay history.");
            return;
        }

        for (PayStatement p : history) {
            System.out.println(
                    p.getPayDate() +
                    " | Gross: " + p.getGross() +
                    " | Deductions: " + p.getDeductions() +
                    " | Net: " + p.getNet()
            );
        }
    }
}
