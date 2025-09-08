package com.etimo.etimo_employee_api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"error", "_embedded"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ValidationErrorResponse(String error, List<FieldErrorDetail> fieldErrors) {}
