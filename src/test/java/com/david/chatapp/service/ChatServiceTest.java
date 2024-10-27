package com.david.chatapp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.david.chatapp.model.Chat;
import com.david.chatapp.model.ChatDto;
import com.david.chatapp.repository.ChatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ChatServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ChatServiceTest {
    @MockBean
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;


    @Test
    @Disabled("TODO: Complete this test")
    void testSaveMessage() {
        String expectedUsername = "testUser";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(expectedUsername);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        ChatDto inputChatDto = new ChatDto();
        inputChatDto.setContent("Hello, world!");

        ChatDto resultChatDto = chatService.saveMessage(inputChatDto);

        assertEquals(expectedUsername, resultChatDto.getSender());

        assertNotNull(resultChatDto.getTimestamp());
        assertDoesNotThrow(() -> LocalDateTime.parse(resultChatDto.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
        verify(chatRepository, times(1)).save(chatCaptor.capture());

        Chat savedChat = chatCaptor.getValue();
        assertEquals(expectedUsername, savedChat.getSender());
        assertEquals("Hello, world!", savedChat.getContent());
        assertEquals(resultChatDto.getTimestamp(), savedChat.getTimestamp());
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetChatHistory() {
        // Arrange
        when(chatRepository.findAllByOrderByTimestampAsc()).thenReturn(new ArrayList<>());

        List<ChatDto> actualChatHistory = chatService.getChatHistory();

        verify(chatRepository).findAllByOrderByTimestampAsc();
        assertTrue(actualChatHistory.isEmpty());
    }


    @Test
    void testGetChatHistory2() {
        // Arrange
        ArrayList<Chat> chatList = new ArrayList<>();
        Chat buildResult = Chat.builder()
                .content("Not all who wander are lost")
                .id("42")
                .sender("Sender")
                .timestamp("Timestamp")
                .build();
        chatList.add(buildResult);
        when(chatRepository.findAllByOrderByTimestampAsc()).thenReturn(chatList);

        List<ChatDto> actualChatHistory = chatService.getChatHistory();
        verify(chatRepository).findAllByOrderByTimestampAsc();
        assertEquals(1, actualChatHistory.size());
        ChatDto getResult = actualChatHistory.getFirst();
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Sender", getResult.getSender());
        assertEquals("Timestamp", getResult.getTimestamp());
    }

    @Test
    void testGetChatHistory3() {
        // Arrange
        ArrayList<Chat> chatList = new ArrayList<>();
        Chat buildResult = Chat.builder()
                .content("Not all who wander are lost")
                .id("42")
                .sender("Sender")
                .timestamp("Timestamp")
                .build();
        chatList.add(buildResult);
        Chat buildResult2 = Chat.builder()
                .content("Not all who wander are lost")
                .id("42")
                .sender("Sender")
                .timestamp("Timestamp")
                .build();
        chatList.add(buildResult2);
        when(chatRepository.findAllByOrderByTimestampAsc()).thenReturn(chatList);

        List<ChatDto> actualChatHistory = chatService.getChatHistory();

        verify(chatRepository).findAllByOrderByTimestampAsc();
        assertEquals(2, actualChatHistory.size());
        ChatDto getResult = actualChatHistory.getFirst();
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Sender", getResult.getSender());
        assertEquals("Timestamp", getResult.getTimestamp());
        assertEquals(getResult, actualChatHistory.get(1));
    }
}
