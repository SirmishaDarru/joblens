package com.joblens.auth;

import com.joblens.security.JwtService;
import com.joblens.user.User;
import com.joblens.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;

    record RegisterRequest(@NotBlank @Email String email, @NotBlank @Size(min=8) String password, @NotBlank String fullName) {}
    record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    record AuthResponse(String token, Long userId, String email, String fullName) {}

    @PostMapping("/register")
    AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        if (users.findByEmail(request.email()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        User user = new User(); user.setEmail(request.email().toLowerCase()); user.setPasswordHash(encoder.encode(request.password())); user.setFullName(request.fullName()); users.save(user);
        return response(user);
    }

    @PostMapping("/login")
    AuthResponse login(@Valid @RequestBody LoginRequest request) {
        User user = users.findByEmail(request.email().toLowerCase()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!encoder.matches(request.password(), user.getPasswordHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        return response(user);
    }
    private AuthResponse response(User user) { return new AuthResponse(jwt.createToken(user.getEmail()), user.getId(), user.getEmail(), user.getFullName()); }
}
