package ru.matvey.socialmediaapi.dto.post;

import lombok.Data;

@Data
public class GetFileRequest {
    private Long postId;
    private String fileName;

}
