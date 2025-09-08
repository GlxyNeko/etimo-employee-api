package com.etimo.etimo_employee_api.controller.v1;

import com.etimo.etimo_employee_api.controller.ApiEndpoints;
import com.etimo.etimo_employee_api.controller.EmployeeModelAssembler;
import com.etimo.etimo_employee_api.model.Employee;
import com.etimo.etimo_employee_api.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(EmployeeModelAssembler.class)
class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @TestBean
    private EmployeeRepository repository;

    static public EmployeeRepository repository() {
        return Mockito.mock(EmployeeRepository.class);
    }

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee1;
    private UUID employee1Id;
    private Employee employee2;
    private UUID employee2Id;

    @BeforeEach
    void setUp() {
        employee1Id = UUID.randomUUID();
        employee1 = new Employee(employee1Id, "Nathalie", "Jonsson", "developer9704@gmail.com");
        employee2Id = UUID.randomUUID();
        employee2 = new Employee(employee2Id, "Ulf", "Kristersson", "ulf.kristersson@riksdagen.se");
    }

    @Test
    void getAllEmployees_whenEmployeesExists_returnsHalCollection() throws Exception {
        when(repository.findAll()).thenReturn(List.of(employee1, employee2));
        mockMvc.perform(MockMvcRequestBuilders.get(ApiEndpoints.EMPLOYEES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.employees[0].firstName", is("Nathalie")))
                .andExpect(jsonPath("$._links.self.href", containsString(ApiEndpoints.EMPLOYEES)));
    }

    @Test
    void getAllEmployees_whenNoEmployees_returnsEmptyHalCollection() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(ApiEndpoints.EMPLOYEES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded").doesNotExist())
                .andExpect(jsonPath("$._links.self.href", containsString(ApiEndpoints.EMPLOYEES)));
    }

    @Test
    void getEmployee_whenExists_returnsEmployeeModel() throws Exception {
        when(repository.findById(employee1Id)).thenReturn(Optional.of(employee1));
        mockMvc.perform(get(ApiEndpoints.EMPLOYEES + ApiEndpoints.BY_ID, employee1Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is(employee1Id.toString())))
                .andExpect(jsonPath("$.firstName", is("Nathalie")))
                .andExpect(jsonPath("$._links.self.href", containsString(employee1Id.toString())));
    }

    @Test
    void getEmployee_whenNotExists_returnsNotFoundHalError() throws Exception {
        when(repository.findById(employee2Id)).thenReturn(Optional.empty());
        mockMvc.perform(get(ApiEndpoints.EMPLOYEES + ApiEndpoints.BY_ID, employee2Id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$._links.self.href", containsString(employee2Id.toString())));
    }

    @Test
    void addEmployee_withValidData_returnsCreated() throws Exception {
        Employee newEmployee = new Employee(null, "Carl XVI", "Gustaf", "carl.xvi.gustaf@kungahuset.se");
        when(repository.existsByEmail(any(String.class))).thenReturn(false);
        when(repository.save(any(Employee.class))).thenReturn(employee1);
        mockMvc.perform(post(ApiEndpoints.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(ApiEndpoints.EMPLOYEES + "/" + employee1Id)))
                .andExpect(jsonPath("$.id").value(employee1Id.toString()));
    }

    @Test
    void addEmployee_withDuplicateEmail_returnsConflictHalError() throws Exception {
        Employee duplicateEmployee = new Employee(null, "Nathalie", "Jonsson", "developer9704@gmail.com");
        when(repository.existsByEmail("developer9704@gmail.com")).thenReturn(true);
        mockMvc.perform(post(ApiEndpoints.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmployee)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.error", containsString("already exists")))
                .andExpect(jsonPath("$._links.employees.href", containsString(ApiEndpoints.EMPLOYEES)));
    }

    @Test
    void removeEmployee_whenExists_returnsNoContent() throws Exception {
        when(repository.existsById(employee1Id)).thenReturn(true);
        mockMvc.perform(delete(ApiEndpoints.EMPLOYEES + ApiEndpoints.BY_ID, employee1Id))
                .andExpect(status().isNoContent());
        verify(repository).deleteById(employee1Id);
    }

    @Test
    void removeEmployee_whenNotExists_returnsNotFoundHalError() throws Exception {
        when(repository.existsById(employee1Id)).thenReturn(false);
        mockMvc.perform(delete(ApiEndpoints.EMPLOYEES + ApiEndpoints.BY_ID, employee1Id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.error").exists());
    }
}