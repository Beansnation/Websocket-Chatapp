package com.david.chatapp.service;

import com.david.chatapp.model.RegisterDto;
import com.david.chatapp.model.ResponseData;
import com.david.chatapp.model.User;
import com.david.chatapp.model.UserRole;
import com.david.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ResponseData> registerUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.username())) {
            return new ResponseEntity<>(
                    new ResponseData("Username is taken", HttpStatus.BAD_REQUEST.value(), false),
                    HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .username(registerDto.username())
                .password(passwordEncoder.encode(registerDto.password()))
                .firstname(registerDto.firstname())
                .lastname(registerDto.lastname())
                .phoneNumber(registerDto.phoneNumber())
                .role(UserRole.ROLE_USER)
                .build();
        userRepository.save(user);
        return new ResponseEntity<>(
                new ResponseData("user registration success", HttpStatus.CREATED.value(), true, user), HttpStatus.CREATED);
    }
}