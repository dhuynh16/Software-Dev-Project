package com.companyz.ems.domain;

import java.time.LocalDate;

public abstract class Person {
    protected String name;
    protected LocalDate dob;
    protected String ssn;
    protected String address;

    public String getName() { return name; }
    public LocalDate getDob() { return dob; }
    public String getSsn() { return ssn; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
}

