package com.companyz.ems.service;

import com.companyz.ems.dao.EmployeeDAO;
import com.companyz.ems.domain.Employee;
import com.companyz.ems.security.Session;
import com.companyz.ems.security.UserRole;
import java.time.LocalDate;
import java.util.List;

public class EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    // Feature 2 (search for editing, HR only)
    public List<Employee> searchForAdmin(Session session,
                                         String name, LocalDate dob,
                                         String ssn, Integer empid) {
        requireHR(session);
        return employeeDAO.search(name, dob, ssn, empid);
    }

    // Feature 3 (search for viewing, employee only)
    // EmployeeService.java

public List<Employee> searchForEmployee(Session session,
String name,
LocalDate dob,
String ssn,
Integer empid) {
// Only general employees use this function
if (session == null || session.getRole() != UserRole.GENERAL_EMPLOYEE) {
throw new SecurityException("General employee only");
}

// 🔓 ALLOW searching anyone:
// Use exactly what they typed as filters, do NOT force session.getEmpid()
return employeeDAO.search(name, dob, ssn, empid);
}


    // Feature 4 (update employee data, HR only)
    public void updateEmployee(Session session, Employee e) {
        requireHR(session);
        employeeDAO.updateEmployee(e);
    }

    // Feature 5 (update salary range, HR only)
    public int updateSalaryRange(Session session,
                                 double min, double max, double percent) {
        requireHR(session);
        if (percent <= 0) throw new IllegalArgumentException("Percent must be > 0");
        return employeeDAO.updateSalaryRange(min, max, percent);
    }

    // Feature 7(d): employees hired within date range (HR only)
    public List<Employee> employeesHiredBetween(Session session,
                                                LocalDate from, LocalDate to) {
        requireHR(session);
        return employeeDAO.hiredBetween(from, to);
    }

    private void requireHR(Session session) {
        if (session == null || session.getRole() != UserRole.HR_ADMIN) {
            throw new SecurityException("HR admin only");
        }
    }

    private void requireEmployee(Session session) {
        if (session == null || session.getRole() != UserRole.GENERAL_EMPLOYEE) {
            throw new SecurityException("General employee only");
        }
    }
    public void createEmployee(Session session, Employee e) {
        requireHR(session);
        employeeDAO.createEmployee(e);
    }
}    

