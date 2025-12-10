package com.companyz.ems.service;

import com.companyz.ems.dao.PayrollDAO;
import com.companyz.ems.domain.PayStatement;
import com.companyz.ems.security.Session;
import com.companyz.ems.security.UserRole;
import java.util.List;

public class PayrollService {

    private final PayrollDAO payrollDAO;

    public PayrollService(PayrollDAO payrollDAO) {
        this.payrollDAO = payrollDAO;
    }

    // 6a: employee sees own pay statement history
    public List<PayStatement> ownPayHistory(Session session) {
        if (session == null || session.getRole() != UserRole.GENERAL_EMPLOYEE) {
            throw new SecurityException("General employee only");
        }
        Integer empid = session.getUser().getEmpid();
        if (empid == null) throw new IllegalStateException("No empid linked");
        return payrollDAO.payHistoryForEmployee(empid);
    }

    // 6b, 6c: HR reports
    public List<String> totalPayByJobTitle(Session session, int year, int month) {
        requireHR(session);  // 👈 this is a private method in this class
        return payrollDAO.totalPayByJobTitleForMonth(year, month);
    }
    
    private void requireHR(Session session) {
        if (session == null || session.getRole() != UserRole.HR_ADMIN) {
            throw new SecurityException("HR admin only");
        }
    }
    


    public List<String> totalPayByDivision(Session session, int year, int month) {
        requireHR(session);
        return payrollDAO.totalPayByDivisionForMonth(year, month);
    }

}

