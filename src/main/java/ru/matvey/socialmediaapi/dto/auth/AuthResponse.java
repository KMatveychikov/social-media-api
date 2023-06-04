package ru.matvey.socialmediaapi.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//TODO: @Data здесь overkill мне кажется
@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
