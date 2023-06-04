package ru.matvey.socialmediaapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.matvey.socialmediaapi.dto.auth.AuthRequest;
import ru.matvey.socialmediaapi.dto.auth.AuthResponse;
import ru.matvey.socialmediaapi.dto.auth.RegisterRequest;
import ru.matvey.socialmediaapi.dto.auth.UserDto;
import ru.matvey.socialmediaapi.model.FriendshipRequest;
import ru.matvey.socialmediaapi.model.User;
import ru.matvey.socialmediaapi.repository.FriendshipRepository;
import ru.matvey.socialmediaapi.repository.UserDtoRepository;
import ru.matvey.socialmediaapi.repository.UserRepository;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserDtoRepository userDtoRepository;
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
            UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getUsername());
            userDtoRepository.save(userDto);
            log.info("userDto {} saved", userDto);
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
                .orElseThrow(() -> {
                    log.warn("user {} not found", request.getEmail());
                    return new UsernameNotFoundException("user not found");
                });
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

    public User getCurrentUser() throws Exception {
        return userRepository.findByEmail(getCurrentUserEmail()).orElseThrow(() -> {
            log.warn("User not found");
            return new Exception("User not found");
        });
    }

    public UserDto getCurrentUserDto() throws Exception {
        return userDtoRepository.findById(getCurrentUser().getId()).orElseThrow(() -> {
            log.warn("User not found");
            return new Exception("User not found");
        });
    }

    public UserDto getUserDtoById(Long id) throws Exception {
        return userDtoRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found");
            return new Exception("User not found");
        });
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();

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
                    .userId(getCurrentUserDto().getId())
                    .friendUserId(friendId)
                    .build();
            friendshipRepository.save(fr);
            addSubscription(getCurrentUserDto().getId(), friendId);
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
                return new Exception("request not found");
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
        List<UserDto> friends = user.getFriends();
        friends.add(getUserDtoById(friendId));
        user.setFriends(friends);
        userRepository.save(user);

    }

    public void addSubscription(Long userId, Long subscriptionUserId) throws Exception {
        User user = getUserById(userId);
        List<UserDto> subs = user.getSubscriptions();
        if (!subs.contains(getUserDtoById(subscriptionUserId))) {
            subs.add(getUserDtoById(subscriptionUserId));
            log.info("subscription {} added to user {}", subscriptionUserId, userId);
        }
        user.setSubscriptions(subs);
        userRepository.save(user);

    }

    public boolean isFriend(Long userId) throws Exception {
        User user = getUserById(userId);
        if (user.getFriends().contains(getCurrentUserDto())) {
            return true;
        } else {
            return false;
        }
    }
}
