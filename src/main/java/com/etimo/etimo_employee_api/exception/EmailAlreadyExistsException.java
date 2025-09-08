package com.etimo.etimo_employee_api.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Employee with email " + email + " already exists");
    }
}
