package com.form76.generator.rest.model;

import com.form76.generator.db.entity.Role;

public record AuthResponse(String id, String name, Role role) {
}