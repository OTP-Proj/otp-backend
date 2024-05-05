package org.otp.otp.controller;

import org.otp.otp.model.dto.*;
import org.otp.otp.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/auth")
@RestController
@CrossOrigin
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> login(@RequestBody @Validated LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        if (authenticationService.validateRegistration(request)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtTokenResponse> refreshToken(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(this.authenticationService.refreshToken(loginRequest.getUserCode()));
    }

}
