package com.lamego.agendapro.controller;

import com.lamego.agendapro.dto.user.request.LoginRequest;
import com.lamego.agendapro.dto.user.response.LoginResponse;
import com.lamego.agendapro.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
