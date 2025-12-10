package com.companyz.ems.ui;

import com.companyz.ems.domain.Employee;
import com.companyz.ems.domain.PayStatement;

import java.util.Date;
import java.util.Scanner;

public class EmployeeInputCollector {

    private Scanner scanner = new Scanner(System.in);

    // Collect basic employee info
    public Employee collectNewEmployee() {

        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter job title: ");
        String job = scanner.nextLine();

        System.out.print("Enter division: ");
        String division = scanner.nextLine();

        System.out.print("Enter base salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();

        return new Employee(id, name, job, division, salary);
    }

    // Collect salary update info
    public double[] collectSalaryUpdateInfo() {

        System.out.print("Enter percentage increase: ");
        double percent = scanner.nextDouble();

        System.out.print("Enter minimum salary: ");
        double min = scanner.nextDouble();

        System.out.print("Enter maximum salary: ");
        double max = scanner.nextDouble();
        scanner.nextLine();

        return new double[] { percent, min, max };
    }

    // Collect pay statement info
    public PayStatement collectPayStatement(int empId) {

        System.out.print("Enter gross pay: ");
        double gross = scanner.nextDouble();

        System.out.print("Enter deductions: ");
        double ded = scanner.nextDouble();
        scanner.nextLine();

        Date now = new Date();

        return new PayStatement(0, empId, now, gross, ded);
    }
}
