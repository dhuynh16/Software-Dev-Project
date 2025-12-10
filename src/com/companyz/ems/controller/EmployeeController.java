package com.companyz.ems.controller;

import com.companyz.ems.domain.Employee;
import com.companyz.ems.security.Session;
import com.companyz.ems.service.EmployeeService;

import java.util.List;

public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // used in sequence: POST /addEmployee(id, name, salary, dept)
    public void addEmployee(Employee e) {
        employeeService.addEmployee(e);
    }

    public void updateEmployee(Session session, Employee e) {
        employeeService.updateEmployee(session, e);
    }

    public Employee viewOwnProfile(Session session) {
        return employeeService.getOwnProfile(session);
    }

    public List<Employee> searchByName(Session session, String name) {
        return employeeService.searchByName(session, name);
    }
}
