package com.david.chatapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


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
}

