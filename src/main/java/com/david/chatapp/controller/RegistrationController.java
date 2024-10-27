package com.david.chatapp.controller;

import com.david.chatapp.model.RegisterDto;
import com.david.chatapp.model.ResponseData;
import com.david.chatapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class RegistrationController {
    private final AuthService userService;

    @PostMapping("register")
    public ResponseEntity<ResponseData> registerUser(@RequestBody @Valid RegisterDto registerDto){
        return userService.registerUser(registerDto);
    }
}