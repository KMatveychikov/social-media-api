package ru.matvey.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.matvey.socialmediaapi.model.Post;
import ru.matvey.socialmediaapi.model.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByAuthor(User author);
}
