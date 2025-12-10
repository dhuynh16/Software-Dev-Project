package com.companyz.ems.domain;

import java.time.LocalDate;

public class PayStatement {
    private final int payId;
    private final int empid;
    private final LocalDate payDate;
    private final double gross;
    private final double deductions;
    private final double net;

    public PayStatement(int payId, int empid,
                        LocalDate payDate, double gross,
                        double deductions, double net) {
        this.payId = payId;
        this.empid = empid;
        this.payDate = payDate;
        this.gross = gross;
        this.deductions = deductions;
        this.net = net;
    }

    public int getPayId() { return payId; }
    public int getEmpid() { return empid; }
    public LocalDate getPayDate() { return payDate; }
    public double getGross() { return gross; }
    public double getDeductions() { return deductions; }
    public double getNet() { return net; }
}


