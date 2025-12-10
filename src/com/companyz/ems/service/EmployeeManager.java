package com.companyz.ems.service;

import com.companyz.ems.dao.EmployeeDAO;
import com.companyz.ems.domain.Employee;

import java.util.List;

public class EmployeeManager {

    private EmployeeDAO employeeDAO = new EmployeeDAO();

    // Add employee (no validation version)
    public String addEmployee(Employee emp) {
        boolean success = employeeDAO.addEmployee(emp);

        if (success)
            return "Employee added successfully.";
        return "Failed to add employee.";
    }

    // Search by ID
    public Employee searchById(int id) {
        return employeeDAO.getEmployeeById(id);
    }

    // Search by name
    public List<Employee> searchByName(String name) {
        return employeeDAO.searchByName(name);
    }

    // Update salary by range
    public String updateSalaryRange(double percent, double min, double max) {

        int updated = employeeDAO.updateSalaryRange(percent, min, max);

        if (updated == 0) {
            return "No employees in the specified range.";
        }

        return updated + " employees received a salary update.";
    }
}
