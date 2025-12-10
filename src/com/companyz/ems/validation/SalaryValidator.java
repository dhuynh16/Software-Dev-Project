package com.companyz.ems.validation;

public class SalaryValidator {

    public void validateRangeAndPercent(double min, double max, double percent) {
        if (percent <= 0) {
            throw new InvalidInputException("Percent must be greater than 0");
        }
        if (min < 0 || max < 0 || min > max) {
            throw new InvalidInputException("Invalid salary range");
        }
    }
}
