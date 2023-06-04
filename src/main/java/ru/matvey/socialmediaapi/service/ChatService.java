package ru.matvey.socialmediaapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.matvey.socialmediaapi.model.Chat;
import ru.matvey.socialmediaapi.model.Message;
import ru.matvey.socialmediaapi.repository.ChatRepository;
import ru.matvey.socialmediaapi.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public ResponseEntity<?> addChat(Long userId) throws Exception {
        try {
        if (userService.isFriend(userId)) {
            Chat chat = Chat.builder()
                    .messages(new ArrayList<>())
                    .members(List.of(userService.getUserDtoById(userId), userService.getCurrentUserDto()))
                    .build();
            chatRepository.save(chat);
            return ResponseEntity.ok(chat);
        } else {
            log.warn("you can't write to a friend who isn't yours");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Exception("you can't write to a friend who isn't yours"));
        } } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    public Chat getChatById(Long chatId) throws Exception {
        return chatRepository.findById(chatId).orElseThrow(() -> {
            log.warn("Chat {} not found", chatId);
            return new Exception("Chat  not found");
        });
    }

    public ResponseEntity<?> addMessage(Long chatId, String message) throws Exception {
        try {
            Chat chat = getChatById(chatId);
            if (chat.getMembers().contains(userService.getCurrentUserDto())) {
                List<Message> messages = chat.getMessages();
                Message mess = Message.builder()
                        .authorName(userService.getCurrentUser().getName())
                        .text(message)
                        .build();
                messageRepository.save(mess);
                messages.add(mess);
                chat.setMessages(messages);
                chatRepository.save(chat);
                return ResponseEntity.ok(chat);
            } else {
                log.warn("you cannot write to a chat that you are not a member of");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Exception("you cannot write to a chat that you are not a member of"));
            }
        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

    public ResponseEntity<?> getMyChats() {
        try {
            String currentUserName = userService.getCurrentUserDto().getName();
            return ResponseEntity.ok(chatRepository.findAll().stream().filter(chat -> chat.getMembers().contains(currentUserName)).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }


    }
}
