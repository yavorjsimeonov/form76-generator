package com.form76.generator.rest.model;

import com.form76.generator.db.entity.Role;

public record AuthResponse(String id, String userName, String firstName, String lastName, String email, Role role) {
}