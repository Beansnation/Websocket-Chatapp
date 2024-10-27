package com.david.chatapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.david.chatapp.filter.jwt.JwtService;
import com.david.chatapp.model.AuthenticateDto;
import com.david.chatapp.model.RegisterDto;
import com.david.chatapp.model.ResponseData;
import com.david.chatapp.model.UserRole;
import com.david.chatapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthServiceImpl.class, PasswordEncoder.class, AuthenticationManager.class,
        UserDetailsService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AuthServiceImplTest {
    @Autowired
    private AuthServiceImpl authServiceImpl;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testRegisterUser() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByUsername(Mockito.any())).thenReturn(true);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new DaoAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtService jwtService = new JwtService();
        AuthServiceImpl authServiceImpl = new AuthServiceImpl(userRepository, passwordEncoder, authenticationManager,
                jwtService, new InMemoryUserDetailsManager());

        ResponseEntity<ResponseData> actualRegisterUserResult = authServiceImpl
                .registerUser(new RegisterDto("janedoe", "iloveyou", "Jane", "6625550144", "Doe"));

        verify(userRepository).existsByUsername("janedoe");
        HttpStatusCode statusCode = actualRegisterUserResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        ResponseData body = actualRegisterUserResult.getBody();
        assert body != null;
        assertEquals("Username is taken", body.getMessage());
        assertNull(body.getData());
        assertEquals(400, body.getStatus());
        assertEquals(400, actualRegisterUserResult.getStatusCode().value());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertFalse(body.isSuccess());
        assertTrue(actualRegisterUserResult.hasBody());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
    }

    @Test
    void testRegisterUser2() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByUsername(Mockito.any())).thenReturn(false);
        com.david.chatapp.model.User buildResult = com.david.chatapp.model.User.builder()
                .firstname("Jane")
                .id("42")
                .lastname("Doe")
                .password("iloveyou")
                .phoneNumber("6625550144")
                .role(UserRole.ROLE_USER)
                .username("janedoe")
                .build();
        when(userRepository.save(Mockito.any())).thenReturn(buildResult);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new DaoAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JwtService jwtService = new JwtService();
        AuthServiceImpl authServiceImpl = new AuthServiceImpl(userRepository, passwordEncoder, authenticationManager,
                jwtService, new InMemoryUserDetailsManager());

        ResponseEntity<ResponseData> actualRegisterUserResult = authServiceImpl
                .registerUser(new RegisterDto("janedoe", "iloveyou", "Jane", "6625550144", "Doe"));

        verify(userRepository).existsByUsername("janedoe");
        verify(userRepository).save(isA(com.david.chatapp.model.User.class));
        HttpStatusCode statusCode = actualRegisterUserResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        ResponseData body = actualRegisterUserResult.getBody();
        assert body != null;
        assertEquals("user registration success", body.getMessage());
        assertNull(body.getData());
        assertEquals(201, body.getStatus());
        assertEquals(201, actualRegisterUserResult.getStatusCode().value());
        assertEquals(HttpStatus.CREATED, statusCode);
        assertTrue(body.isSuccess());
        assertTrue(actualRegisterUserResult.hasBody());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
    }

    @Test
    void testAuthenticateUser() throws AuthenticationException {
        // Arrange
        when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        when(userDetailsService.loadUserByUsername(Mockito.any()))
                .thenReturn(new User("janedoe", "iloveyou", new ArrayList<>()));
        when(jwtService.generateToken(Mockito.any())).thenReturn("ABC123");

        // Act
        ResponseEntity<ResponseData> actualAuthenticateUserResult = authServiceImpl
                .authenticateUser(new AuthenticateDto("janedoe", "iloveyou"));

        // Assert
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(authenticationManager).authenticate(isA(Authentication.class));
        verify(userDetailsService).loadUserByUsername("janedoe");
        ResponseData body = actualAuthenticateUserResult.getBody();
        assert body != null;
        Object data = body.getData();
        assertInstanceOf(Map.class, data);
        HttpStatusCode statusCode = actualAuthenticateUserResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        assertEquals("Authentication Successful", body.getMessage());
        assertEquals(200, body.getStatus());
        assertEquals(200, actualAuthenticateUserResult.getStatusCode().value());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(body.isSuccess());
        assertTrue(actualAuthenticateUserResult.hasBody());
        assertTrue(actualAuthenticateUserResult.getHeaders().isEmpty());
    }

    @Test
    void testAuthenticateUser2() {
        ResponseEntity<ResponseData> actualAuthenticateUserResult = authServiceImpl.authenticateUser(null);

        HttpStatusCode statusCode = actualAuthenticateUserResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        ResponseData body = actualAuthenticateUserResult.getBody();
        assert body != null;
        assertEquals("Invalid Username or Password", body.getMessage());
        assertNull(body.getData());
        assertEquals(401, body.getStatus());
        assertEquals(401, actualAuthenticateUserResult.getStatusCode().value());
        assertEquals(HttpStatus.UNAUTHORIZED, statusCode);
        assertFalse(body.isSuccess());
        assertTrue(actualAuthenticateUserResult.hasBody());
        assertTrue(actualAuthenticateUserResult.getHeaders().isEmpty());
    }

    @Test
    void testAuthenticateUser3() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        when(userDetailsService.loadUserByUsername(Mockito.any()))
                .thenReturn(new User("janedoe", "iloveyou", new ArrayList<>()));
        when(jwtService.generateToken(Mockito.any())).thenReturn("ABC123");

        ResponseEntity<ResponseData> actualAuthenticateUserResult = authServiceImpl
                .authenticateUser(new AuthenticateDto("janedoe", "iloveyou"));

        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(authenticationManager).authenticate(isA(Authentication.class));
        verify(userDetailsService).loadUserByUsername("janedoe");
        ResponseData body = actualAuthenticateUserResult.getBody();
        assert body != null;
        Object data = body.getData();
        assertInstanceOf(Map.class, data);
        HttpStatusCode statusCode = actualAuthenticateUserResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        assertEquals("Authentication Successful", body.getMessage());
        assertEquals(200, body.getStatus());
        assertEquals(200, actualAuthenticateUserResult.getStatusCode().value());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(body.isSuccess());
    }

    @Test
    void testAuthenticateNullUser() {
        ResponseEntity<ResponseData> actualAuthenticateUserResult = authServiceImpl.authenticateUser(null);

        HttpStatusCode statusCode = actualAuthenticateUserResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        ResponseData body = actualAuthenticateUserResult.getBody();
        assert body != null;
        assertEquals("Invalid Username or Password", body.getMessage());
        assertNull(body.getData());
        assertEquals(401, body.getStatus());
        assertEquals(401, actualAuthenticateUserResult.getStatusCode().value());
        assertEquals(HttpStatus.UNAUTHORIZED, statusCode);
        assertFalse(body.isSuccess());
    }
}
