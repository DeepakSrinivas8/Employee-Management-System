package com.imaginnovate.employee_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String employeeId;

    private String firstName;
    private String lastName;
    private String email;

    @ElementCollection
    private List<String> phoneNumbers;

    private LocalDate doj;
    private Double salary;
} 