package com.david.chatapp.config;

import com.david.chatapp.filter.jwt.JwtService;
import com.david.chatapp.service.AuthChannelInterceptorAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // JwtService is used to handle JWT token processing (validation, extraction of claims, etc.)
    private final JwtService jwtTokenUtil;

    // UserDetailsService is used to load user-specific data for authentication based on the JWT token
    private final UserDetailsService userService;

    /**
     * Registers the WebSocket endpoints that clients will use to connect to the WebSocket server.
     *
     * @param registry - the StompEndpointRegistry to register the endpoint
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Adds a WebSocket endpoint at /ws-chat for clients to connect, with SockJS fallback for browsers that don't support WebSocket
        registry.addEndpoint("/ws-chat")
                .withSockJS();  // SockJS provides a fallback option for WebSocket communication if native WebSocket is not supported
    }

    /**
     * Configures the message broker options for WebSocket communication, allowing messages to be routed to the correct destination.
     *
     * @param registry - the MessageBrokerRegistry to configure the message broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory message broker that listens for messages prefixed with "/topic"
        registry.enableSimpleBroker("/topic");

        // Set the prefix "/app" for messages bound for methods annotated with @MessageMapping in controllers
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Configures the client2 inbound channel to apply an interceptor that validates JWT tokens on incoming WebSocket messages.
     * This method intercepts messages on the client-to-server channel to ensure they are authenticated.
     *
     * @param registration - the ChannelRegistration object to register the interceptor
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Adds an interceptor that will authenticate messages using a JWT token thereby ensuring only registered users are sending and receiving messages
        registration.interceptors(new AuthChannelInterceptorAdapter(userService, jwtTokenUtil));
    }
}

