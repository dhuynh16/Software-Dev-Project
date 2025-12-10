package com.companyz.ems.service;

import com.companyz.ems.dao.PayStatementDAO;
import com.companyz.ems.domain.PayStatement;

import java.util.List;

public class PayStatementManager {

    private PayStatementDAO dao = new PayStatementDAO();

    public String addPayStatement(PayStatement ps) {
        boolean success = dao.addPayStatement(ps);

        if (success)
            return "Pay statement created.";
        return "Failed to create pay statement.";
    }

    public List<PayStatement> getPayHistory(int empId) {
        return dao.getPayHistory(empId);
    }
}
