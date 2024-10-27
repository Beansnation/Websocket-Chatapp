package com.david.chatapp.controller;

import com.david.chatapp.model.ChatDto;
import com.david.chatapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    // Injecting the ChatService to handle saving and retrieving chat messages
    private final ChatService chatService;

    /**
     * Handles incoming chat messages sent to the "/chat.send" destination.
     * This method is annotated with @MessageMapping to indicate that it processes messages sent from clients
     * to the server at the specified destination. After receiving the message, it saves the message to the database
     * or in-memory storage via the ChatService, then sends the message to all subscribers of the "/topic/public"
     * destination using the messagingTemplate.
     *
     * @param message - the chat message payload sent by the client (wrapped in ChatDto)
     * @return ChatDto - the saved chat message that will be sent back to clients subscribed to "/topic/public"
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")  // Sends the response to all subscribers of "/topic/public"
    public ChatDto sendMessage(@Payload ChatDto message) {
        // Save the received message using the chatService and return the chat message so it's automatically sent to "/topic/public" by @SendTo
        return chatService.saveMessage(message);
    }
}