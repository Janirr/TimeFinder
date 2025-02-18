package com.poznan.put.rest.webservice.restapi.security;

import com.poznan.put.rest.webservice.restapi.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }

    @PostMapping("/login/student")
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.studentLogin(loginRequest));
    }

    @PostMapping("/login/tutor")
    public ResponseEntity<?> tutorLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.tutorLogin(loginRequest));

    }
}
