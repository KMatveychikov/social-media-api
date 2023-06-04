package ru.matvey.socialmediaapi.dto.post;

import lombok.Data;

//TODO: @Data здесь overkill мне кажется
@Data
public class GetFileRequest {
    private Long postId;
    private String fileName;

}
