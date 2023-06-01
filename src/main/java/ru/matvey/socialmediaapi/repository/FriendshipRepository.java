package ru.matvey.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.matvey.socialmediaapi.model.FriendshipRequest;
@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipRequest, Long> {
}
