package com.companyz.ems.controller;

import com.companyz.ems.security.Session;
import com.companyz.ems.service.SalaryService;

public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    public int increaseSalaries(Session session,
                                double min, double max, double percent) {
        return salaryService.increaseSalaries(session, min, max, percent);
    }
}
