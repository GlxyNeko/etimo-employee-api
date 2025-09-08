package com.etimo.etimo_employee_api.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(UUID id) {
        super("Employee with id " + id + " not found");
    }
}
