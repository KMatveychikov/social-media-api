package ru.matvey.socialmediaapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.socialmediaapi.dto.auth.AuthRequest;
import ru.matvey.socialmediaapi.dto.auth.AuthResponse;
import ru.matvey.socialmediaapi.dto.auth.RegisterRequest;
import ru.matvey.socialmediaapi.service.UserService;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor

public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse authenticate(
            @RequestBody AuthRequest request
    ) {
        return service.authenticate(request);
    }

    @PostMapping("/make_friend")
    public ResponseEntity<?> makeFriendshipRequest(@RequestParam Long friendId) {
        return service.makeFriendshipRequest(friendId);
    }

    @PostMapping("/accept_friend")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam Long friendRequestId){
        return service.acceptFriendship(friendRequestId);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        return service.getAllUsers();
    }

}
