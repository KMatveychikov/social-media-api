package ru.matvey.socialmediaapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import ru.matvey.socialmediaapi.dto.post.AddPostRequest;
import ru.matvey.socialmediaapi.dto.post.UpdatePostRequest;
import ru.matvey.socialmediaapi.enums.Role;
import ru.matvey.socialmediaapi.model.User;
import ru.matvey.socialmediaapi.service.PostService;
import ru.matvey.socialmediaapi.service.UserService;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class PostControllerTest {
    @Mock
    PostService postService;
    @Mock
    UserService userService;
    @InjectMocks
    PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPost() throws Exception {
        when(postService.addPost(any(), any())).thenReturn(null);
        when(userService.getCurrentUser()).thenReturn(new User(Long.valueOf(1), "username", "email", "password", Role._USER, List.of(), List.of()));

        ResponseEntity<?> result = postController.addPost(new AddPostRequest("title", "text"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetAllPosts() {
        when(postService.getAllPosts(PageRequest.of(1,10))).thenReturn(null);

        ResponseEntity<?> result = postController.getAllPosts(1, 10);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetPostFeed() {
        when(postService.getPostsBySubscriptions(any())).thenReturn(null);

        ResponseEntity<?> result = postController.getPostFeed(1, 10);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdatePost() throws Exception {
        when(postService.updatePost(any())).thenReturn(null);

        ResponseEntity<?> result = postController.updatePost(new UpdatePostRequest());
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAddFileToPost() throws Exception {
        when(postService.addFileToPost(anyLong(), any())).thenReturn(null);

        ResponseEntity<?> result = postController.addFileToPost(Long.valueOf(1), null);
        Assertions.assertEquals(null, result);
    }
}

