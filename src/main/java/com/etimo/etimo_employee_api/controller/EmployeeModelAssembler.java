package com.etimo.etimo_employee_api.controller;

import com.etimo.etimo_employee_api.controller.v1.EmployeeController;
import com.etimo.etimo_employee_api.model.Employee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    @NonNull
    public EntityModel<Employee> toModel(@NonNull Employee employee) {
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployee(employee.id())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
    }
}