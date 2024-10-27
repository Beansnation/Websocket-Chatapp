package com.david.chatapp.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterDto(

        @NotNull(message = "username cannot be null")
        @NotEmpty(message = "username cannot be empty")
        String username,

        @NotNull(message = "password cannot be null")
        @NotEmpty(message = "password cannot be empty")
        String password,

        @NotNull(message = "firstName cannot be null")
        @NotEmpty(message = "firstName cannot be empty")
        String firstname,

        @NotNull(message = "phone number cannot be null")
        @NotEmpty(message = "phone number cannot be empty")
        String phoneNumber,

        @NotNull(message = "lastName cannot be null")
        @NotEmpty(message = "lastName cannot be empty")
        String lastname
) {}
