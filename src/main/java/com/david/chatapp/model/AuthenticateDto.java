package com.david.chatapp.model;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenticateDto(
        @NotEmpty(message = "username cannot be empty")
        @NotNull(message = "username cannot be null")
        String username,

        @NotEmpty(message = "username cannot be empty")
        @NotNull(message = "username cannot be null")
        String password
) {}
