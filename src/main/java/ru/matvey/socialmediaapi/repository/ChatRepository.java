package ru.matvey.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.matvey.socialmediaapi.model.Chat;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
