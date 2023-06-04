package ru.matvey.socialmediaapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

//TODO: мёртвый код
//import static org.mockito.Mockito.*;
class ChatServiceTest {
    //TODO: давай префикс уберём
    @Mock
    ru.matvey.socialmediaapi.service.UserService userService;
    //TODO: префикс
    @Mock
    ru.matvey.socialmediaapi.repository.ChatRepository chatRepository;
    //TODO: префикс
    @Mock
    ru.matvey.socialmediaapi.repository.MessageRepository messageRepository;
    //TODO: префикс
    @Mock
    org.slf4j.Logger log;
    //TODO: префикс
    @InjectMocks
    ru.matvey.socialmediaapi.service.ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddChat() {
        when(userService.getCurrentUserDto()).thenReturn(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name"));
        when(userService.getUserDtoById(anyLong())).thenReturn(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name"));
        when(userService.isFriend(anyLong())).thenReturn(true);

        org.springframework.http.ResponseEntity<?> result = chatService.addChat(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetChatById() {
        ru.matvey.socialmediaapi.model.Chat result = chatService.getChatById(Long.valueOf(1));
        Assertions.assertEquals(new ru.matvey.socialmediaapi.model.Chat(Long.valueOf(1), java.util.List.of(null), java.util.List.of(null)), result);
    }

    @Test
    void testAddMessage() {
        when(userService.getCurrentUser()).thenReturn(new ru.matvey.socialmediaapi.model.User(Long.valueOf(1), "username", "email", "password", ru.matvey.socialmediaapi.enums.Role._USER, java.util.List.of(null), java.util.List.of(null)));
        when(userService.getCurrentUserDto()).thenReturn(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name"));

        org.springframework.http.ResponseEntity<?> result = chatService.addMessage(Long.valueOf(1), "message");
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetMyChats() {
        when(userService.getCurrentUserDto()).thenReturn(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name"));

        org.springframework.http.ResponseEntity<?> result = chatService.getMyChats();
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme