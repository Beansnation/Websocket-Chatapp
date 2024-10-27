package com.david.chatapp.controller;

import com.david.chatapp.model.ChatDto;
import com.david.chatapp.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatService chatService;

    /**
     * Retrieves the history of all chat messages.
     * This method handles HTTP GET requests to "/chat/history". It calls the ChatService to retrieve
     * a list of all previously saved chat messages, which is then returned as a JSON response.
     *
     * @return List<ChatDto> - a list of chat messages representing the chat history
     */
    @GetMapping("history")
    public List<ChatDto> getChatHistory() {
        // Retrieves and returns the chat history from the chatService
        return chatService.getChatHistory();
    }
}
