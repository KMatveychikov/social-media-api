package ru.matvey.socialmediaapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.matvey.socialmediaapi.service.ChatService;

import static org.mockito.Mockito.*;

class ChatControllerTest {
    @Mock
    ChatService chatService;
    @InjectMocks
    ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMyChats() {
        when(chatService.getMyChats()).thenReturn(null);

        ResponseEntity<?> result = chatController.getMyChats();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAddChat() throws Exception {
        when(chatService.addChat(anyLong())).thenReturn(null);

        ResponseEntity<?> result = chatController.addChat(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testSendMessage() throws Exception {
        when(chatService.addMessage(anyLong(), anyString())).thenReturn(null);

        ResponseEntity<?> result = chatController.sendMessage(Long.valueOf(1), "message");
        Assertions.assertEquals(null, result);
    }
}

