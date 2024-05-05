package org.otp.otp.service;


import org.otp.otp.model.dto.JwtTokenResponse;
import org.otp.otp.model.dto.LoginRequest;
import org.otp.otp.model.dto.RegisterRequest;

public interface AuthenticationService {

    JwtTokenResponse login(LoginRequest request);

    boolean validateRegistration(RegisterRequest request);

    JwtTokenResponse refreshToken(String userCode);
}
