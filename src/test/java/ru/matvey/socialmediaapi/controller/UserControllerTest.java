package ru.matvey.socialmediaapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.matvey.socialmediaapi.dto.auth.AuthRequest;
import ru.matvey.socialmediaapi.dto.auth.AuthResponse;
import ru.matvey.socialmediaapi.dto.auth.RegisterRequest;
import ru.matvey.socialmediaapi.dto.auth.UserDto;
import ru.matvey.socialmediaapi.enums.Role;
import ru.matvey.socialmediaapi.model.User;
import ru.matvey.socialmediaapi.service.UserService;

import java.util.List;

import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    UserService service;
    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        when(service.register(any())).thenReturn(null);

        ResponseEntity<?> result = userController.register(new RegisterRequest("name", "email", "password", Role._USER));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAuthenticate() {
        when(service.authenticate(any())).thenReturn(new AuthResponse("token"));

        AuthResponse result = userController.authenticate(new AuthRequest("email", "password"));
        Assertions.assertEquals(new AuthResponse("token"), result);
    }

    @Test
    void testMakeFriendshipRequest() {
        when(service.makeFriendshipRequest(anyLong())).thenReturn(null);

        ResponseEntity<?> result = userController.makeFriendshipRequest(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAcceptFriendRequest() {
        when(service.acceptFriendship(anyLong())).thenReturn(null);

        ResponseEntity<?> result = userController.acceptFriendRequest(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetAllUsers() {
        when(service.getAllUsers()).thenReturn(List.of(new User(Long.valueOf(1), "username", "email", "password", Role._USER, List.of(new UserDto(Long.valueOf(1), "email", "name")), List.of(new UserDto(Long.valueOf(1), "email", "name")))));

        List<User> result = userController.getAllUsers();
        Assertions.assertEquals(List.of(new User(Long.valueOf(1), "username", "email", "password", Role._USER, List.of(new UserDto(Long.valueOf(1), "email", "name")), List.of(new UserDto(Long.valueOf(1), "email", "name")))), result);
    }
}

