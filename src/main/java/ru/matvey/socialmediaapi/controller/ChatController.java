package ru.matvey.socialmediaapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matvey.socialmediaapi.repository.ChatRepository;
import ru.matvey.socialmediaapi.service.ChatService;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/")
    public ResponseEntity<?> getMyChats(){
        return chatService.getMyChats();
    }

    @PostMapping("/add_chat")
    public ResponseEntity<?> addChat(@RequestParam Long userId) throws Exception {
       return chatService.addChat(userId);
    }

    @PostMapping("/send_message")
    public ResponseEntity<?> sendMessage(@RequestParam Long chatId, @RequestParam String message) throws Exception {
        return chatService.addMessage(chatId, message);
    }

}
