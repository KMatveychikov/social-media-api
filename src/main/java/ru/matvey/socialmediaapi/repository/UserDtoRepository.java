package ru.matvey.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.matvey.socialmediaapi.dto.auth.UserDto;

@Repository
public interface UserDtoRepository extends JpaRepository<UserDto, Long> {
}
