package com.imaginnovate.employee_management_system.service.impl;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imaginnovate.employee_management_system.dto.EmployeeDTO;
import com.imaginnovate.employee_management_system.dto.TaxDeductionDTO;
import com.imaginnovate.employee_management_system.entity.Employee;
import com.imaginnovate.employee_management_system.exception.EmployeeNotFoundException;
import com.imaginnovate.employee_management_system.repository.EmployeeRepository;
import com.imaginnovate.employee_management_system.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmployeeId(employeeDTO.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee = employeeRepository.save(employee);

        EmployeeDTO response = new EmployeeDTO();
        BeanUtils.copyProperties(employee, response);
        return response;
    }

    @Override
    public TaxDeductionDTO calculateTaxDeductions(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        // Calculate yearly salary based on joining date
        double yearlySalary = calculateYearlySalary(employee);
        
        // Calculate tax based on slabs
        double taxAmount = calculateTaxAmount(yearlySalary);
        
        // Calculate cess if applicable
        double cessAmount = calculateCessAmount(yearlySalary);

        TaxDeductionDTO taxDeductionDTO = new TaxDeductionDTO();
        taxDeductionDTO.setEmployeeId(employee.getEmployeeId());
        taxDeductionDTO.setFirstName(employee.getFirstName());
        taxDeductionDTO.setLastName(employee.getLastName());
        taxDeductionDTO.setYearlySalary(yearlySalary);
        taxDeductionDTO.setTaxAmount(taxAmount);
        taxDeductionDTO.setCessAmount(cessAmount);

        return taxDeductionDTO;
    }

    private double calculateYearlySalary(Employee employee) {
        LocalDate currentDate = LocalDate.now();
        LocalDate financialYearStart = getFinancialYearStart(currentDate);
        LocalDate financialYearEnd = getFinancialYearEnd(currentDate);

        // If employee joined after financial year start, calculate pro-rata
        if (employee.getDoj().isAfter(financialYearStart)) {
            long daysInYear = ChronoUnit.DAYS.between(financialYearStart, financialYearEnd);
            long daysWorked = ChronoUnit.DAYS.between(employee.getDoj(), financialYearEnd);
            return (employee.getSalary() * 12 * daysWorked) / daysInYear;
        }

        return employee.getSalary() * 12;
    }

    private LocalDate getFinancialYearStart(LocalDate date) {
        if (date.getMonthValue() < 4) {
            return LocalDate.of(date.getYear() - 1, Month.APRIL, 1);
        }
        return LocalDate.of(date.getYear(), Month.APRIL, 1);
    }

    private LocalDate getFinancialYearEnd(LocalDate date) {
        if (date.getMonthValue() < 4) {
            return LocalDate.of(date.getYear(), Month.MARCH, 31);
        }
        return LocalDate.of(date.getYear() + 1, Month.MARCH, 31);
    }

    private double calculateTaxAmount(double yearlySalary) {
        double taxAmount = 0.0;

        if (yearlySalary > 1000000) {
            taxAmount += (yearlySalary - 1000000) * 0.2; // 20% for > 10L
            yearlySalary = 1000000;
        }

        if (yearlySalary > 500000) {
            taxAmount += (yearlySalary - 500000) * 0.1; // 10% for 5L-10L
            yearlySalary = 500000;
        }

        if (yearlySalary > 250000) {
            taxAmount += (yearlySalary - 250000) * 0.05; // 5% for 2.5L-5L
        }

        return taxAmount;
    }

    private double calculateCessAmount(double yearlySalary) {
        if (yearlySalary > 2500000) {
            return (yearlySalary - 2500000) * 0.02; // 2% cess for > 25L
        }
        return 0.0;
    }
} 