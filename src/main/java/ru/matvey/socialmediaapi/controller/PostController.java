package ru.matvey.socialmediaapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.matvey.socialmediaapi.dto.post.AddPostRequest;
import ru.matvey.socialmediaapi.dto.post.UpdatePostRequest;
import ru.matvey.socialmediaapi.model.Post;
import ru.matvey.socialmediaapi.service.PostService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody AddPostRequest request) throws IOException {
        return postService.addPost(request);
    }

    //TODO: может попробуем без _ ?
    // не очень хороший контракт, постов может быть миллион, в таком случае лучше использовать пагинацию
    @GetMapping("/get_all")
    public ResponseEntity<?> getAllPosts() {
        return postService.getAllPosts();
    }
    //TODO: может попробуем без _ ?
    @GetMapping("/post_feed")
    public ResponseEntity<?> getPostFeed(){
        return postService.getPostsBySubscriptions();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePost(UpdatePostRequest request) throws Exception {
        return postService.updatePost(request);
    }

    //TODO: может попробуем без _ ?
    @PostMapping("/add_file")
    public ResponseEntity<?> addFileToPost(@RequestParam Long postId,@RequestParam MultipartFile file) throws Exception {
       return postService.addFileToPost(postId, file);
    }


}
