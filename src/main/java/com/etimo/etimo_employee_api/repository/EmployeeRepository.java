package com.etimo.etimo_employee_api.repository;

import com.etimo.etimo_employee_api.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    Employee save(Employee employee);
    Optional<Employee> findById(UUID id);
    List<Employee> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByEmail(String name);
}
