package ru.matvey.socialmediaapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.matvey.socialmediaapi.dto.post.AddPostRequest;
import ru.matvey.socialmediaapi.dto.post.GetFileRequest;
import ru.matvey.socialmediaapi.dto.post.UpdatePostRequest;
import ru.matvey.socialmediaapi.model.FileInfo;
import ru.matvey.socialmediaapi.model.Post;
import ru.matvey.socialmediaapi.model.User;
import ru.matvey.socialmediaapi.repository.FileInfoRepository;
import ru.matvey.socialmediaapi.repository.PostRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final UserService authService;
    private final PostRepository postRepository;
    private final FileInfoRepository fileInfoRepository;

    private final Environment env;

//    @Value("${pathToFiles}")
//    private final String pathToFiles;

    public ResponseEntity<?> addPost(AddPostRequest request, User user) throws IOException {
        try {
            Post post = Post.builder()
                    .title(request.getTitle())
                    .text(request.getText())
                    .author(user)
                    .files(new ArrayList<>())
                    .build();
            postRepository.save(post);
            log.info("Post {} created", post.getId());
            Files.createDirectory(Path.of(env.getProperty("pathToFiles") + post.getId()));
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }


    }

    public ResponseEntity<?> getAllPosts(PageRequest pageRequest) {
        try {
            Page<Post> page = postRepository.findAll(pageRequest);
            return ResponseEntity.ok(page.getContent());
        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

    public ResponseEntity<?> updatePost(UpdatePostRequest request) throws Exception {
        try {
            Post post = getPostById(request.getPostId());
            if (authService.getCurrentUser() == post.getAuthor()) {
                if (!Objects.equals(request.getTitle(), "")) {
                    post.setTitle(request.getTitle());
                }
                if (!Objects.equals(request.getText(), "")) {
                    post.setText(request.getText());
                }
                postRepository.save(post);
                log.info("Post {} updated", post);
            } else {
                log.warn("You can't edit other people's posts");
                throw new Exception("You can't edit other people's posts");

            }
            return ResponseEntity.ok(post);

        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }


    }

    public Post getPostById(Long id) throws Exception {
        return postRepository.findById(id).orElseThrow(() -> {
            log.warn("Post {} not found", id);
            return new Exception("Post not found");
        });
    }

    public ResponseEntity<?> deletePost(Long postId) throws Exception {
        Post post = getPostById(postId);
        if (post.getAuthor() == authService.getCurrentUser()) {
            postRepository.deleteById(postId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            log.warn("You can't edit other people's posts");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Exception("You can't edit other people's posts"));
        }
    }

    public List<Post> getPostsByAuthor(Long authorId) throws Exception {
        User author = authService.getUserById(authorId);
        return postRepository.findPostsByAuthor(author);
    }

    public ResponseEntity<?> getPostsBySubscriptions(PageRequest pageRequest) {
        try {
            User user = authService.getCurrentUser();
            List<Post> postFeed = new ArrayList<>();
            user.getSubscriptions().forEach(usr -> {
                try {
                    postFeed.addAll(getPostsByAuthor(usr.getId()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            PagedListHolder<Post> pagedListHolder = new PagedListHolder<Post>(postFeed);
            pagedListHolder.setPage(pageRequest.getPageNumber());
            pagedListHolder.setPageSize(pageRequest.getPageSize());

            return ResponseEntity.ok(pagedListHolder.getPageList());

        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    public ResponseEntity<?> addFileToPost(Long postId, MultipartFile file) throws Exception {
        try {
            Post post = getPostById(postId);
            if (authService.getCurrentUser() == post.getAuthor()) {
                String path = env.getProperty("pathToFiles") + postId + "//" + file.getOriginalFilename();
                try {
                    file.transferTo(new File(path));
                } catch (Exception e) {
                    log.warn(e.toString());
                    throw new IOException(e.toString());
                }
                List<FileInfo> files = post.getFiles();
                FileInfo fileInfo = FileInfo.builder()
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .filePath(path)
                        .build();
                fileInfoRepository.save(fileInfo);
                files.add(fileInfo);
                post.setFiles(files);
                postRepository.save(post);
                log.info("File {} saved to post", file.getOriginalFilename());
            } else {
                log.warn("You can't edit other people's posts");
                throw new Exception("You can't edit other people's posts");
            }
            return ResponseEntity.ok(post);

        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }


    }

    public ResponseEntity<?> getFile(GetFileRequest request) throws Exception {
        Post post = getPostById(request.getPostId());
        FileInfo fileInfo = post.getFiles().stream().filter(fi -> Objects.equals(fi.getFileName(), request.getFileName())).findFirst().orElseThrow(() -> {
            log.warn("File {} not found", request.getFileName());
            return new FileNotFoundException();
        });
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(fileInfo.getContentType()))
                .body(Files.readAllBytes(new File(fileInfo.getFilePath()).toPath()));
    }
}
