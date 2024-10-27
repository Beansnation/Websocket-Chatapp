package com.david.chatapp.service;

import com.david.chatapp.filter.jwt.JwtService;
import com.david.chatapp.model.AuthenticateDto;
import com.david.chatapp.model.RegisterDto;
import com.david.chatapp.model.ResponseData;
import com.david.chatapp.model.User;
import com.david.chatapp.model.UserRole;
import com.david.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

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
                new ResponseData("user registration success", HttpStatus.CREATED.value(), true), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseData> authenticateUser(AuthenticateDto authenticateDto) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticateDto.username());
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticateDto.username(),
                            authenticateDto.password()
                    )
            );

            // **3. Set Authentication in Security Context**
            // Store the authentication object in the SecurityContextHolder for subsequent handshake requests
            // This establishes the user's authentication state within the application.
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            // **4. Generate JWT Token**
            // Create a JWT token for the authenticated user.
            // The token contains registered user information and is signed using a secret key.
            // Tokens are stateless and can be used for subsequent requests, including WebSocket connections.
            String token = jwtService.generateToken(userDetails);

            // **5. Prepare Response with Token**
            // Create a map to hold the token and include it in the response.
            // The client will use this token to authenticate future requests.
            Map<String, String> map = new HashMap<>();
            map.put("token", token);

            return new ResponseEntity<>(
                    new ResponseData("Authentication Successful", HttpStatus.OK.value(), true, map),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseData("Invalid Username or Password", HttpStatus.UNAUTHORIZED.value(), false),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}