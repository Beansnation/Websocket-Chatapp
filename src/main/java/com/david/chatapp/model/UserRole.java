package com.david.chatapp.model;


import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

}
