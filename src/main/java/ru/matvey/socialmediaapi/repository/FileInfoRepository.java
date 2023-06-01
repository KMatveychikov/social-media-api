package ru.matvey.socialmediaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.matvey.socialmediaapi.model.FileInfo;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
}
