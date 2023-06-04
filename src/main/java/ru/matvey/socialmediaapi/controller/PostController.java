package ru.matvey.socialmediaapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.matvey.socialmediaapi.dto.post.AddPostRequest;
import ru.matvey.socialmediaapi.dto.post.UpdatePostRequest;
import ru.matvey.socialmediaapi.service.PostService;
import ru.matvey.socialmediaapi.service.UserService;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody AddPostRequest request) throws Exception {
        return postService.addPost(request, userService.getCurrentUser());
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllPosts(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        return postService.getAllPosts(PageRequest.of(page, size));
    }

    @GetMapping("/post_feed")
    public ResponseEntity<?> getPostFeed(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        return postService.getPostsBySubscriptions(PageRequest.of(page, size));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePost(UpdatePostRequest request) throws Exception {
        return postService.updatePost(request);
    }

    @PostMapping("/add_file")
    public ResponseEntity<?> addFileToPost(@RequestParam Long postId, @RequestParam MultipartFile file) throws Exception {
        return postService.addFileToPost(postId, file);
    }


}
