package com.imaginnovate.employee_management_system.controller;

import com.imaginnovate.employee_management_system.dto.EmployeeDTO;
import com.imaginnovate.employee_management_system.dto.TaxDeductionDTO;
import com.imaginnovate.employee_management_system.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Implementation will be added later
        return null;
    }

    @GetMapping("/{employeeId}/tax-deductions")
    public ResponseEntity<TaxDeductionDTO> getTaxDeductions(@PathVariable String employeeId) {
        // Implementation will be added later
        return null;
    }
} 