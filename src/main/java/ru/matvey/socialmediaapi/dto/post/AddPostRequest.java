package ru.matvey.socialmediaapi.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddPostRequest {
    private String title, text;
}
