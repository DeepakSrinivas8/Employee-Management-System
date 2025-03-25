package com.imaginnovate.employee_management_system.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private String error;
    private int status;
} 