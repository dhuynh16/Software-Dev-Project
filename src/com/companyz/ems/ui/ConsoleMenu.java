package com.companyz.ems.ui;

import com.companyz.ems.auth.EmployeeAuthenticator;
import com.companyz.ems.auth.HRAdminAuthenticator;
import com.companyz.ems.domain.Employee;
import com.companyz.ems.domain.PayStatement;
import com.companyz.ems.service.EmployeeDataStore;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private Scanner scanner = new Scanner(System.in);
    private EmployeeDataStore dataStore = new EmployeeDataStore();
    private EmployeeInputCollector collector = new EmployeeInputCollector();

    public void start() {

        while (true) {

            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Login as HR Admin");
            System.out.println("2. Login as Employee");
            System.out.println("3. Exit");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1)
                handleHRLogin();
            else if (choice == 2)
                handleEmployeeLogin();
            else if (choice == 3)
                break;
            else
                System.out.println("Invalid option.");
        }
    }

    // HR ADMIN LOGIN HANDLER
    private void handleHRLogin() {

        HRAdminAuthenticator auth = new HRAdminAuthenticator();

        System.out.print("Enter email: ");
        String user = scanner.nextLine();

        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        if (auth.login(user, pass)) {
            System.out.println("HR Admin login successful.");
            hrMenu();
        } else {
            System.out.println("Invalid login.");
        }
    }

    // EMPLOYEE LOGIN HANDLER
    private void handleEmployeeLogin() {

        EmployeeAuthenticator auth = new EmployeeAuthenticator();

        System.out.print("Enter email: ");
        String user = scanner.nextLine();

        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        if (auth.login(user, pass)) {
            System.out.println("Employee login successful.");
            employeeMenu();
        } else {
            System.out.println("Invalid login.");
        }
    }

    // HR ADMIN MENU
    private void hrMenu() {

        while (true) {

            System.out.println("\n--- HR Admin Menu ---");
            System.out.println("1. Search Employee");
            System.out.println("2. Add Employee");
            System.out.println("3. Update Salary Range");
            System.out.println("4. Create Pay Statement");
            System.out.println("5. Logout");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> searchEmployee();
                case 2 -> addEmployee();
                case 3 -> updateSalaryRange();
                case 4 -> createPayStatement();
                case 5 -> {
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    // EMPLOYEE MENU
    private void employeeMenu() {

        System.out.print("Enter your employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        while (true) {
            System.out.println("\n--- Employee Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. View Pay History");
            System.out.println("3. Logout");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> viewProfile(empId);
                case 2 -> viewPayHistory(empId);
                case 3 -> {
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    // HR Admin actions
    private void searchEmployee() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();

        List<Employee> results = dataStore.searchByName(name);

        if (results.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("\n--- Search Results ---");
        for (Employee e : results) {
            System.out.println(
                "EmpID: " + e.getEmpid() +
                " | Name: " + e.getFullName() +
                " | DOB: " + (e.getDob() != null ? e.getDob() : "N/A") +
                " | SSN: " + (e.getSsn() != null ? e.getSsn() : "N/A") +
                " | Title: " + e.getJobTitle() +
                " | Division: " + e.getDivision() +
                " | Status: " + e.getStatus() +
                " | Salary: $" + e.getSalary()
            );
        }
        
    }

    private void addEmployee() {
        Employee emp = collector.collectNewEmployee();
        System.out.println(dataStore.addEmployee(emp));
    }

    private void updateSalaryRange() {
        double[] values = collector.collectSalaryUpdateInfo();
        System.out.println(dataStore.updateSalaryRange(values[0], values[1], values[2]));
    }

    private void createPayStatement() {
        System.out.print("Enter Employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        PayStatement ps = collector.collectPayStatement(empId);
        System.out.println(dataStore.addPayStatement(ps));
    }

    // Employee actions
    private void viewProfile(int empId) {
        Employee emp = dataStore.getEmployeeById(empId);
        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\n--- Your Profile ---");
        System.out.println("ID: " + emp.getEmployeeId());
        System.out.println("Name: " + emp.getName());
        System.out.println("Salary: " + emp.getSalary());
    }

    private void viewPayHistory(int empId) {
        List<PayStatement> history = dataStore.getPayHistory(empId);

        if (history.isEmpty()) {
            System.out.println("No pay history found.");
            return;
        }

        System.out.println("\n--- Pay History ---");
        for (PayStatement ps : history) {
            System.out.println("Date: " + ps.getPayDate() +
                    " | Gross: " + ps.getGrossPay() +
                    " | Net: " + ps.getNetPay());
        }
    }
}
