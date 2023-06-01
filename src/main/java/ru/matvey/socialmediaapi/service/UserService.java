package ru.matvey.socialmediaapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.matvey.socialmediaapi.dto.auth.AuthRequest;
import ru.matvey.socialmediaapi.dto.auth.AuthResponse;
import ru.matvey.socialmediaapi.dto.auth.RegisterRequest;
import ru.matvey.socialmediaapi.model.FriendshipRequest;
import ru.matvey.socialmediaapi.model.User;
import ru.matvey.socialmediaapi.repository.FriendshipRepository;
import ru.matvey.socialmediaapi.repository.UserRepository;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register(RegisterRequest request) {
        try {
            User user = User.builder()
                    .email(request.getEmail())
                    .username(request.getName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();
            log.info("new user register {}", user);
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            AuthResponse authResponse = AuthResponse.builder()
                    .token(jwtToken)
                    .build();
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }

    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        log.info("User {} has authorized", user.getName());
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserEmail()).orElseThrow();
    }


    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }

    public User getUserById(Long userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User not found");
            return new Exception("User not found");
        });
    }

    public ResponseEntity<?> makeFriendshipRequest(Long friendId) {
        try {
            FriendshipRequest fr = FriendshipRequest.builder()
                    .userId(getCurrentUser().getId())
                    .friendUserId(friendId)
                    .build();
            friendshipRepository.save(fr);
            addSubscription(getCurrentUser().getId(), friendId);
            return ResponseEntity.ok(fr);
        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    public ResponseEntity<?> acceptFriendship(Long friendRequestId) {
        try {
            FriendshipRequest fr = friendshipRepository.findById(friendRequestId).orElseThrow(() -> {
                log.warn("request with id {} not found", friendRequestId);
                return new RuntimeException();
            });
            if (Objects.equals(fr.getFriendUserId(), getCurrentUser().getId())) {
                addFriendship(fr.getUserId(), fr.getFriendUserId());
                addFriendship(fr.getFriendUserId(), fr.getUserId());
                addSubscription(fr.getFriendUserId(), fr.getUserId());
            }
            friendshipRepository.delete(fr);
            return ResponseEntity.ok(getUserById(fr.getUserId()));

        } catch (Exception e) {
            log.warn(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

    }


    public void addFriendship(Long userId, Long friendId) throws Exception {
        User user = getUserById(userId);
        List<User> friends = user.getFriends();
        friends.add(getUserById(friendId));
        user.setFriends(friends);
        userRepository.save(user);

    }

    public void addSubscription(Long userId, Long subscriptionUserId) throws Exception {
        User user = getUserById(userId);
        List<User> subs = user.getSubscriptions();
        subs.add(getUserById(subscriptionUserId));
        user.setSubscriptions(subs);
        userRepository.save(user);
        log.info("subscription {} added to user {}", subscriptionUserId, userId);
    }


}
