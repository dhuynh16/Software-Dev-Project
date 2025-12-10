package com.companyz.ems.service;

import com.companyz.ems.dao.EmployeeDAO;
import com.companyz.ems.security.Session;
import com.companyz.ems.security.UserRole;
import com.companyz.ems.validation.NoEmployeesFoundException;
import com.companyz.ems.validation.SalaryValidator;

public class SalaryService {

    private final EmployeeDAO employeeDAO;
    private final SalaryValidator validator;

    public SalaryService(EmployeeDAO employeeDAO, SalaryValidator validator) {
        this.employeeDAO = employeeDAO;
        this.validator = validator;
    }

    public int increaseSalaries(Session session,
                                double min, double max, double percent) {
        requireAdmin(session);

        // Scenario 1: invalid input
        validator.validateRangeAndPercent(min, max, percent);

        // Scenario 2 & 3: DAO call and result
        int updated = employeeDAO.updateSalaryRange(min, max, percent);
        if (updated == 0) {
            throw new NoEmployeesFoundException("No employees in the specified range.");
        }
        return updated;
    }

    private void requireAdmin(Session session) {
        if (session == null || session.getRole() != UserRole.HR_ADMIN) {
            throw new SecurityException("Admin access required");
        }
    }
}
