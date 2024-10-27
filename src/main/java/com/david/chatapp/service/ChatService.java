package com.david.chatapp.service;

import com.david.chatapp.model.ChatDto;

import java.util.List;

public interface ChatService {

    ChatDto saveMessage(ChatDto chatMessage);
    List<ChatDto> getChatHistory();
}