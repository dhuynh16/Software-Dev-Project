package com.companyz.ems.domain;

public class JobTitleTotal {
    private String jobTitle;
    private double totalPay;

    public JobTitleTotal(String jobTitle, double totalPay) {
        this.jobTitle = jobTitle;
        this.totalPay = totalPay;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getTotalPay() {
        return totalPay;
    }
}

