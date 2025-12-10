package com.companyz.ems.controller;

import com.companyz.ems.domain.PayStatement;
import com.companyz.ems.security.Session;
import com.companyz.ems.service.PayrollService;

import java.time.LocalDate;
import java.util.List;

public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    public void createPayStatement(Session session, PayStatement p) {
        payrollService.createPayStatement(session, p);
    }

    public List<PayStatement> viewOwnPayHistory(Session session,
                                                LocalDate from, LocalDate to) {
        return payrollService.getOwnPayHistory(session, from, to);
    }
}

