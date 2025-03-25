package com.imaginnovate.employee_management_system.dto;

import lombok.Data;

@Data
public class TaxDeductionDTO {
    private String employeeId;
    private String firstName;
    private String lastName;
    private Double yearlySalary;
    private Double taxAmount;
    private Double cessAmount;
} 