package com.alexdev.controllers;

import com.alexdev.domain.user.User;
import com.alexdev.dtos.request.user.AuthenticateDTO;
import com.alexdev.dtos.request.user.RegisterDTO;
import com.alexdev.dtos.response.user.TokenDTO;
import com.alexdev.repositories.UserRepository;
import com.alexdev.security.jwt.TokenService;
import com.alexdev.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody AuthenticateDTO authenticateDTO) {

        var username = new UsernamePasswordAuthenticationToken(authenticateDTO.login(), authenticateDTO.password());
        try {
            var auth = authenticationManager.authenticate(username);
            String token = tokenService.generateToken((User) auth.getPrincipal());
            return ResponseEntity.ok().body(new TokenDTO(token));
        }
        catch (AuthenticationException exception) {
            throw new RuntimeException("Invalid username and password", exception);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {

        if(userService.loadUserByUsername(registerDTO.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        User newUser = new User(registerDTO.login(), encryptedPassword, registerDTO.role());
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}

