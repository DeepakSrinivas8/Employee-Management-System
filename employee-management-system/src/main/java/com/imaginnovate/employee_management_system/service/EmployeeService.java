package com.imaginnovate.employee_management_system.service;

import com.imaginnovate.employee_management_system.dto.EmployeeDTO;
import com.imaginnovate.employee_management_system.dto.TaxDeductionDTO;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    TaxDeductionDTO calculateTaxDeductions(String employeeId);
} 