package com.companyz.ems.validation;

import com.companyz.ems.dao.EmployeeDAO;

public class EmployeeValidator {

    private final EmployeeDAO employeeDAO;

    public EmployeeValidator(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public void checkEmployeeExistsOrThrow(int empId) {
        if (employeeDAO.employeeExists(empId)) {
            throw new DuplicateIdException("Employee ID already exists");
        }
    }
}
