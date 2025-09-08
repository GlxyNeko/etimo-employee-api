package com.etimo.etimo_employee_api.model;

import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Relation(collectionRelation = "employees", itemRelation = "employee")
public record Employee(UUID id, String firstName, String lastName, String email) {}
