package com.etimo.etimo_employee_api.exception;

import com.etimo.etimo_employee_api.controller.ApiEndpoints;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RepresentationModel<?>> employeeNotFoundHandler(EmployeeNotFoundException exception, HttpServletRequest request) {
        return createHalEmployeeErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ResponseBody
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<RepresentationModel<?>> emailAlreadyExistsHandler(EmailAlreadyExistsException exception, HttpServletRequest request) {
        return createHalEmployeeErrorResponse(exception, HttpStatus.CONFLICT, request);
    }

    private ResponseEntity<RepresentationModel<?>> createHalEmployeeErrorResponse(RuntimeException exception, HttpStatus status, HttpServletRequest request) {
        var errorPayload = Map.of("error", exception.getMessage());
        RepresentationModel<?> errorModel = RepresentationModel.of(errorPayload);
        errorModel.add(Link.of(request.getRequestURI()).withSelfRel());
        errorModel.add(Link.of(ApiEndpoints.EMPLOYEES).withRel("employees"));
        return ResponseEntity.status(status).body(errorModel);
    }
}
