package ru.matvey.socialmediaapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//TODO: мёртвый код
//import static org.mockito.Mockito.*;

//TODO: когда мы тестируем контроллеры, мы должны проверять как минимум 2 вещи:
// 1) код ответа
// 2) реквест
// 3) респонс
// Надо немного допилить
class UserControllerTest {
    //TODO: давай префикс уберём
    @Mock
    ru.matvey.socialmediaapi.service.UserService service;
    //TODO: префикс
    @InjectMocks
    ru.matvey.socialmediaapi.controller.UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        when(service.register(any())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = userController.register(new ru.matvey.socialmediaapi.dto.auth.RegisterRequest("name", "email", "password", ru.matvey.socialmediaapi.enums.Role._USER));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAuthenticate() {
        when(service.authenticate(any())).thenReturn(new ru.matvey.socialmediaapi.dto.auth.AuthResponse("token"));

        ru.matvey.socialmediaapi.dto.auth.AuthResponse result = userController.authenticate(new ru.matvey.socialmediaapi.dto.auth.AuthRequest("email", "password"));
        Assertions.assertEquals(new ru.matvey.socialmediaapi.dto.auth.AuthResponse("token"), result);
    }

    @Test
    void testMakeFriendshipRequest() {
        when(service.makeFriendshipRequest(anyLong())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = userController.makeFriendshipRequest(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAcceptFriendRequest() {
        when(service.acceptFriendship(anyLong())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = userController.acceptFriendRequest(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetAllUsers() {
        when(service.getAllUsers()).thenReturn(java.util.List.of(new ru.matvey.socialmediaapi.model.User(Long.valueOf(1), "username", "email", "password", ru.matvey.socialmediaapi.enums.Role._USER, java.util.List.of(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name")), java.util.List.of(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name")))));

        java.util.List<ru.matvey.socialmediaapi.model.User> result = userController.getAllUsers();
        Assertions.assertEquals(java.util.List.of(new ru.matvey.socialmediaapi.model.User(Long.valueOf(1), "username", "email", "password", ru.matvey.socialmediaapi.enums.Role._USER, java.util.List.of(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name")), java.util.List.of(new ru.matvey.socialmediaapi.dto.auth.UserDto(Long.valueOf(1), "email", "name")))), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme