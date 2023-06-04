package ru.matvey.socialmediaapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.matvey.socialmediaapi.dto.auth.UserDto;
import ru.matvey.socialmediaapi.enums.Role;
import ru.matvey.socialmediaapi.model.Chat;
import ru.matvey.socialmediaapi.model.User;
import ru.matvey.socialmediaapi.repository.ChatRepository;
import ru.matvey.socialmediaapi.repository.MessageRepository;

import java.util.List;

import static org.mockito.Mockito.*;

class ChatServiceTest {
    @Mock
    UserService userService;
    @Mock
    ChatRepository chatRepository;
    @Mock
    MessageRepository messageRepository;
    @Mock
    Logger log;
    @InjectMocks
    ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddChat() throws Exception {
        when(userService.getCurrentUserDto()).thenReturn(new UserDto(Long.valueOf(1), "email", "name"));
        when(userService.getUserDtoById(anyLong())).thenReturn(new UserDto(Long.valueOf(1), "email", "name"));
        when(userService.isFriend(anyLong())).thenReturn(true);

        UserDto userDto = new UserDto(Long.valueOf(1), "email", "name");
        Chat chat = new Chat(null, List.of(), List.of(userDto, userDto));

        ResponseEntity<?> result = chatService.addChat(Long.valueOf(1));
        Assertions.assertEquals(ResponseEntity.ok(chat), result);
    }

    @Test
    void testGetChatById() throws Exception {
        Chat chat = new Chat(1L, List.of(), List.of());
        when(userService.getCurrentUserDto()).thenReturn(new UserDto(1L, "email", "name"));
        when(userService.getUserDtoById(anyLong())).thenReturn(new UserDto(1L, "email", "name"));
        when(userService.isFriend(anyLong())).thenReturn(true);

        when(chatService.addChat(1L)).thenReturn(ResponseEntity.ok(any()));
        when(chatRepository.save(chat)).thenReturn(chat);

        Chat result = chatService.getChatById(1L);
        Assertions.assertEquals(chat, result);
    }

    @Test
    void testAddMessage() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User(1L, "username", "email", "password", Role._USER, List.of(null), List.of(null)));
        when(userService.getCurrentUserDto()).thenReturn(new UserDto(1L, "email", "name"));

        ResponseEntity<?> result = chatService.addMessage(1L, "message");
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetMyChats() throws Exception {
        when(userService.getCurrentUserDto()).thenReturn(new UserDto(Long.valueOf(1), "email", "name"));

        ResponseEntity<?> result = chatService.getMyChats();
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme