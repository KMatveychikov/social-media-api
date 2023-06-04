package ru.matvey.socialmediaapi.dto.post;

import lombok.Data;

//TODO: @Data здесь overkill мне кажется
@Data
public class UpdatePostRequest {
    private Long postId;
    private String title;
    private String text;
}
