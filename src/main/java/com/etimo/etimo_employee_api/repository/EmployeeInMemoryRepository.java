package com.etimo.etimo_employee_api.repository;

import com.etimo.etimo_employee_api.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmployeeInMemoryRepository implements EmployeeRepository {
    private final ConcurrentHashMap<UUID, Employee> employees = new ConcurrentHashMap<>();

    public Employee save(Employee employee) {
        var newEmployee = employee;
        if (newEmployee.id() == null) {
            newEmployee = new Employee(
                    UUID.randomUUID(),
                    employee.firstName(),
                    employee.lastName(),
                    employee.email()
            );
        }
        employees.put(newEmployee.id(), newEmployee);
        return newEmployee;
    }

    public Optional<Employee> findById(UUID id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    @Override
    public void deleteById(UUID id) {
        employees.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return employees.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employees.values()
                .stream()
                .anyMatch(employee -> employee.email().equalsIgnoreCase(email)); // According to RFC 5321, the host part of the email should normally be case-sensitive.
    }
}
