package ru.matvey.socialmediaapi.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.matvey.socialmediaapi.controller.UserController;
import ru.matvey.socialmediaapi.dto.auth.AuthRequest;
import ru.matvey.socialmediaapi.dto.auth.RegisterRequest;
import ru.matvey.socialmediaapi.dto.post.AddPostRequest;
import ru.matvey.socialmediaapi.dto.post.GetFileRequest;
import ru.matvey.socialmediaapi.dto.post.UpdatePostRequest;
import ru.matvey.socialmediaapi.enums.Role;
import ru.matvey.socialmediaapi.model.Post;
import ru.matvey.socialmediaapi.model.User;
import ru.matvey.socialmediaapi.repository.FileInfoRepository;
import ru.matvey.socialmediaapi.repository.FriendshipRepository;
import ru.matvey.socialmediaapi.repository.PostRepository;
import ru.matvey.socialmediaapi.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private FileInfoRepository fileInfoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendshipRepository friendshipRepository;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    private  JwtService jwtService;
    @Mock
    private  AuthenticationManager authenticationManager;
    Post mockPost;
    User user;
    AddPostRequest addPostRequest;
    AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, friendshipRepository, passwordEncoder, jwtService, authenticationManager);
        postService = new PostService(
                userService,
                postRepository,
                fileInfoRepository);
        userService.register(new RegisterRequest("testUser", "testUser@example.com", "12345", Role._USER));
        //userService.authenticate(new AuthRequest("testUser@example.com", "12345"));
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
   void addPostTest() throws IOException {
        user = new User(1L, "username","username@example.com", "12345",  Role._USER, List.of(),List.of());
        mockPost = new Post(1L, "title", "text", new ArrayList<>(), user);
        addPostRequest = new AddPostRequest("title", "text");
        mock(Post.class);
        mock(PostRepository.class);
        when(postRepository.save(mockPost)).thenReturn(mockPost);

    }
}
