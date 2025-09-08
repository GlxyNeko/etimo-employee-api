package com.etimo.etimo_employee_api.controller.v1;

import com.etimo.etimo_employee_api.controller.ApiEndpoints;
import com.etimo.etimo_employee_api.controller.EmployeeModelAssembler;
import com.etimo.etimo_employee_api.exception.EmailAlreadyExistsException;
import com.etimo.etimo_employee_api.exception.EmployeeNotFoundException;
import com.etimo.etimo_employee_api.model.Employee;
import com.etimo.etimo_employee_api.repository.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(ApiEndpoints.EMPLOYEES)
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Employee>> getAllEmployees() {
        List<EntityModel<Employee>> employees = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(
                employees,
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel()
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Employee>> addEmployee(@RequestBody Employee employee) {
        if (repository.existsByEmail(employee.email())) {
            throw new EmailAlreadyExistsException(employee.email());
        }
        var savedEmployee = repository.save(employee);
        var entityModel = assembler.toModel(savedEmployee);

        return ResponseEntity.created(
                entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()
        ).body(entityModel);
    }

    @GetMapping(ApiEndpoints.BY_ID)
    public EntityModel<Employee> getEmployee(@PathVariable(ApiEndpoints.ID) UUID id) {
        var employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employee);
    }

    @DeleteMapping(ApiEndpoints.BY_ID)
    public ResponseEntity<?> deleteEmployee(@PathVariable(ApiEndpoints.ID) UUID id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
