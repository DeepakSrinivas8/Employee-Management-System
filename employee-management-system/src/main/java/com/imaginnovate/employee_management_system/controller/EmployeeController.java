package com.imaginnovate.employee_management_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imaginnovate.employee_management_system.dto.EmployeeDTO;
import com.imaginnovate.employee_management_system.dto.TaxDeductionDTO;
import com.imaginnovate.employee_management_system.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "APIs for managing employee details and tax calculations")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @Operation(summary = "Create a new employee", description = "Creates a new employee with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Employee ID already exists")
    })
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}/tax-deductions")
    @Operation(summary = "Get tax deductions", description = "Calculates and returns tax deductions for the specified employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tax deductions calculated successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<TaxDeductionDTO> getTaxDeductions(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable String employeeId) {
        TaxDeductionDTO taxDeductions = employeeService.calculateTaxDeductions(employeeId);
        return ResponseEntity.ok(taxDeductions);
    }
} 