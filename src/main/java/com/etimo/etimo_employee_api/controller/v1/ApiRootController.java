package com.etimo.etimo_employee_api.controller.v1;

import com.etimo.etimo_employee_api.controller.ApiEndpoints;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(ApiEndpoints.API_BASE + ApiEndpoints.V1)
public class ApiRootController {
    @GetMapping
    public RepresentationModel<?> getApiRoot() {
        var model = new RepresentationModel<>();
        model.add(
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees")
        );
        model.add(
                linkTo(methodOn(ApiRootController.class).getApiRoot()).withSelfRel()
        );
        return model;
    }
}
