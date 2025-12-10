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

    public static void main(String[] args) {
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

        Scanner sc = new Scanner(System.in);

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
            hrMenu(sc, session, employeeService, payrollService);
        } else {
            employeeMenu(sc, session, employeeService, payrollService);
        }

        sc.close();
    }

    private static void hrMenu(Scanner sc, Session session,
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
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1 -> {
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
                        for (Employee e : results) {
                            System.out.println(e.getEmpid() + " - " + e.getFullName() +
                                    " | " + e.getJobTitle() + " | " + e.getDivision() +
                                    " | $" + e.getSalary());
                        }
                    }
                    case 2 -> {
                        System.out.print("EmpID to update: ");
                        int empid = Integer.parseInt(sc.nextLine());
                        List<Employee> list = employeeService.searchForAdmin(session, null, null, null, empid);
                        if (list.isEmpty()) {
                            System.out.println("No employee found.");
                            break;
                        }
                        Employee e = list.get(0);
                        System.out.println("Current email: " + e.getEmail());
                        System.out.print("New email (or blank to keep): ");
                        String email = sc.nextLine();
                        if (!email.isBlank()) e.setEmail(email);

                        System.out.println("Current division: " + e.getDivision());
                        System.out.print("New division (or blank): ");
                        String div = sc.nextLine();
                        if (!div.isBlank()) e.setDivision(div);

                        System.out.println("Current job title: " + e.getJobTitle());
                        System.out.print("New job title (or blank): ");
                        String jt = sc.nextLine();
                        if (!jt.isBlank()) e.setJobTitle(jt);

                        System.out.println("Current status: " + e.getStatus());
                        System.out.print("New status (or blank): ");
                        String st = sc.nextLine();
                        if (!st.isBlank()) e.setStatus(st);

                        System.out.println("Current salary: " + e.getSalary());
                        System.out.print("New salary (or blank): ");
                        String salStr = sc.nextLine();
                        if (!salStr.isBlank()) e.setSalary(Double.parseDouble(salStr));

                        employeeService.updateEmployee(session, e);
                        System.out.println("Employee updated.");
                    }
                    case 3 -> {
                        System.out.print("Min salary: ");
                        double min = Double.parseDouble(sc.nextLine());
                        System.out.print("Max salary: ");
                        double max = Double.parseDouble(sc.nextLine());
                        System.out.print("Percent increase (e.g., 3.2): ");
                        double pct = Double.parseDouble(sc.nextLine());
                        int updated = employeeService.updateSalaryRange(session, min, max, pct);
                        System.out.println("Updated " + updated + " employees.");
                    }
                    case 4 -> {
                        System.out.print("Year (YYYY): ");
                        int year = Integer.parseInt(sc.nextLine());
                        System.out.print("Month (1-12): ");
                        int month = Integer.parseInt(sc.nextLine());
                        for (String row : payrollService.totalPayByJobTitle(session, year, month)) {
                            System.out.println(row);
                        }
                    }
                    case 5 -> {
                        System.out.print("Year (YYYY): ");
                        int year = Integer.parseInt(sc.nextLine());
                        System.out.print("Month (1-12): ");
                        int month = Integer.parseInt(sc.nextLine());
                        for (String row : payrollService.totalPayByDivision(session, year, month)) {
                            System.out.println(row);
                        }
                    }
                    case 6 -> {
                        System.out.print("From date (YYYY-MM-DD): ");
                        LocalDate from = LocalDate.parse(sc.nextLine());
                        System.out.print("To date (YYYY-MM-DD): ");
                        LocalDate to = LocalDate.parse(sc.nextLine());
                        List<Employee> hired = employeeService.employeesHiredBetween(session, from, to);
                        for (Employee e : hired) {
                            System.out.println(e.getEmpid() + " - " + e.getFullName() +
                                    " | Hired: " + e.getHireDate());
                        }
                    }
                    case 0 -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void employeeMenu(Scanner sc, Session session,
                                     EmployeeService employeeService,
                                     PayrollService payrollService) {
        while (true) {
            System.out.println("\n--- Employee Menu ---");
            System.out.println("1) Search my info");
            System.out.println("2) View my pay history");
            System.out.println("0) Exit");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1 -> {
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
                        for (Employee e : results) {
                            System.out.println(e.getEmpid() + " - " + e.getFullName() +
                                    " | " + e.getJobTitle() + " | " + e.getDivision() +
                                    " | $" + e.getSalary());
                        }
                        if (results.isEmpty()) {
                            System.out.println("No matching records (or not your record).");
                        }
                    }
                    case 2 -> {
                        List<PayStatement> history = payrollService.ownPayHistory(session);
                        for (PayStatement p : history) {
                            System.out.println(p.getPayDate() +
                                    " | Gross: " + p.getGross() +
                                    " | Net: " + p.getNet());
                        }
                        if (history.isEmpty()) {
                            System.out.println("No pay history.");
                        }
                    }
                    case 0 -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }
}

