package ru.matvey.socialmediaapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import static org.mockito.Mockito.*;
class ChatControllerTest {
    @Mock
    ru.matvey.socialmediaapi.service.ChatService chatService;
    @InjectMocks
    ru.matvey.socialmediaapi.controller.ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMyChats(){
        when(chatService.getMyChats()).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = chatController.getMyChats();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAddChat(){
        when(chatService.addChat(anyLong())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = chatController.addChat(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testSendMessage(){
        when(chatService.addMessage(anyLong(), anyString())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = chatController.sendMessage(Long.valueOf(1), "message");
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme