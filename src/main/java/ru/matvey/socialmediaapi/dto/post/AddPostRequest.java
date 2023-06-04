package ru.matvey.socialmediaapi.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

//TODO: @Data здесь overkill мне кажется
@Data
@AllArgsConstructor
public class AddPostRequest {
    //TODO: обычно так стараются не писать
    private String title, text;
}
