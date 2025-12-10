package com.companyz.ems.service;

import com.companyz.ems.domain.Employee;
import com.companyz.ems.domain.PayStatement;

import java.util.List;

public class EmployeeDataStore {

    private EmployeeManager employeeManager = new EmployeeManager();
    private PayStatementManager payManager = new PayStatementManager();

    // Employee actions
    public String addEmployee(Employee emp) {
        return employeeManager.addEmployee(emp);
    }

    public Employee getEmployeeById(int id) {
        return employeeManager.searchById(id);
    }

    public List<Employee> searchByName(String name) {
        return employeeManager.searchByName(name);
    }

    public String updateSalaryRange(double percent, double min, double max) {
        return employeeManager.updateSalaryRange(percent, min, max);
    }

    // Pay actions
    public String addPayStatement(PayStatement ps) {
        return payManager.addPayStatement(ps);
    }

    public List<PayStatement> getPayHistory(int empId) {
        return payManager.getPayHistory(empId);
    }
}
