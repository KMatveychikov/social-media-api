package ru.matvey.socialmediaapi.dto.post;

import lombok.Data;

@Data
public class UpdatePostRequest {
    private Long postId;
    private String title;
    private String text;
}
