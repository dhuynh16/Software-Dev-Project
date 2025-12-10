package com.companyz.ems.domain;

import java.time.LocalDate;

public class Employee {

    private int empid;
    private String fname;
    private String lname;
    private LocalDate dob;      // 👈 make sure this exists
    private String ssn;         // 👈 and this
    private LocalDate hireDate;
    private double salary;
    private String email;
    private String division;
    private String jobTitle;
    private String status;

    public Employee(int empid,
                    String fname,
                    String lname,
                    LocalDate dob,
                    String ssn,
                    LocalDate hireDate,
                    double salary,
                    String email,
                    String division,
                    String jobTitle,
                    String status) {
        this.empid = empid;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.ssn = ssn;
        this.hireDate = hireDate;
        this.salary = salary;
        this.email = email;
        this.division = division;
        this.jobTitle = jobTitle;
        this.status = status;
    }

    public int getEmpid() { return empid; }
    public String getFullName() { return fname + " " + lname; }
    public LocalDate getDob() { return dob; }      // 👈 getter
    public String getSsn() { return ssn; }         // 👈 getter
    public LocalDate getHireDate() { return hireDate; }
    public double getSalary() { return salary; }
    public String getEmail() { return email; }
    public String getDivision() { return division; }
    public String getJobTitle() { return jobTitle; }
    public String getStatus() { return status; }

    public void setEmail(String email) { this.email = email; }
    public void setDivision(String division) { this.division = division; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setStatus(String status) { this.status = status; }
    public void setSalary(double salary) { this.salary = salary; }
}



