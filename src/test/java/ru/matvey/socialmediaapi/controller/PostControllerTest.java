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
class PostControllerTest {
    //TODO: давай префикс уберём
    @Mock
    ru.matvey.socialmediaapi.service.PostService postService;
    //TODO: префикс
    @Mock
    ru.matvey.socialmediaapi.service.UserService userService;
    //TODO: префикс
    @InjectMocks
    ru.matvey.socialmediaapi.controller.PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPost() {
        when(postService.addPost(any(), any())).thenReturn(null);
        when(userService.getCurrentUser()).thenReturn(new ru.matvey.socialmediaapi.model.User(Long.valueOf(1), "username", "email", "password", ru.matvey.socialmediaapi.enums.Role._USER, java.util.List.of(null), java.util.List.of(null)));

        org.springframework.http.ResponseEntity<?> result = postController.addPost(new ru.matvey.socialmediaapi.dto.post.AddPostRequest("title", "text"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetAllPosts() {
        when(postService.getAllPosts(any())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = postController.getAllPosts(0, 0);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetPostFeed() {
        when(postService.getPostsBySubscriptions(any())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = postController.getPostFeed(0, 0);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdatePost() {
        when(postService.updatePost(any())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = postController.updatePost(new ru.matvey.socialmediaapi.dto.post.UpdatePostRequest());
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAddFileToPost() {
        when(postService.addFileToPost(anyLong(), any())).thenReturn(null);

        org.springframework.http.ResponseEntity<?> result = postController.addFileToPost(Long.valueOf(1), null);
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme