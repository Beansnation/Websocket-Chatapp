package com.david.chatapp.service;

import com.david.chatapp.model.Chat;
import com.david.chatapp.model.ChatDto;
import com.david.chatapp.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatMessageRepository;


    public ChatDto saveMessage(ChatDto chatMessage) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        chatMessage.setSender(username);
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Chat entity = Chat.builder()
                .sender(chatMessage.getSender())
                .content(chatMessage.getContent())
                .timestamp(chatMessage.getTimestamp())
                .build();
        chatMessageRepository.save(entity);
        return chatMessage;
    }

    public List<ChatDto> getChatHistory() {
        return chatMessageRepository.findAllByOrderByTimestampAsc()
                .stream()
                .map(chat -> new ChatDto(chat.getSender(), chat.getContent(), chat.getTimestamp()))
                .toList();
    }
}
