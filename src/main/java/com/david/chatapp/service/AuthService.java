package com.david.chatapp.service;

import com.david.chatapp.model.RegisterDto;
import com.david.chatapp.model.ResponseData;
import org.springframework.http.ResponseEntity;

public interface AuthService{

    ResponseEntity<ResponseData> registerUser(RegisterDto registerDto);
}