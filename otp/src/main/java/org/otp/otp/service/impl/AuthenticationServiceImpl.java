package org.otp.otp.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.otp.otp.exception.UserExistException;
import org.otp.otp.exception.UserNotFoundException;
import org.otp.otp.model.dto.JwtTokenResponse;
import org.otp.otp.model.dto.LoginRequest;
import org.otp.otp.model.dto.RegisterRequest;
import org.otp.otp.model.entity.JwtTokenEntity;
import org.otp.otp.model.entity.UserEntity;
import org.otp.otp.repository.JwtTokenRepository;
import org.otp.otp.repository.UserRepository;
import org.otp.otp.security.JwtService;
import org.otp.otp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     JwtService jwtService,
                                     JwtTokenRepository jwtTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtTokenResponse login(LoginRequest loginRequest) {
        var userEntOpt = this.userRepository.findByUserCode(loginRequest.getUserCode());

        userEntOpt.orElseThrow(UserNotFoundException::new);

        var userEnt = userEntOpt.get();

        String encodedPassword = userEnt.getPassword();
        String encodedIncomingPassword = passwordEncoder.encode(loginRequest.getPassword());

        if (!passwordEncoder.matches(loginRequest.getPassword(), encodedPassword)) {
            throw new UserNotFoundException();
        }

        var userEntity = userEntOpt.get();

        return getJwtTokenResponseIfJwtTokenIsGenerated(userEntity);
    }

    @Override
    @Transactional
    public boolean validateRegistration(RegisterRequest request) {
        checkUserExistence(request.getUserCode());
        insertUserEntity(request);
        return Boolean.TRUE;
    }

    private void checkUserExistence(String userCode) {
        this.userRepository.findByUserCode(userCode)
                .ifPresent(user -> {
                    throw new UserExistException();
                });
    }

    private JwtTokenEntity revokeAllTokensAndGenerateNewToken(UserEntity userEntity) {
        revokeAllTokenByUser(userEntity);
        return insertJwtToken(userEntity);
    }

    private JwtTokenResponse getJwtTokenResponseIfJwtTokenIsGenerated(UserEntity userEntity) {
        JwtTokenEntity jwt = revokeAllTokensAndGenerateNewToken(userEntity);

        return JwtTokenResponse.builder()
                .jwt(jwt.getToken())
                .build();
    }

    @Override
    @Transactional
    public JwtTokenResponse refreshToken(String userCode) {
        var userEntityOptional = userRepository.findByUserCode(userCode);
        userEntityOptional.orElseThrow(UserNotFoundException::new);
        var userEntity = userEntityOptional.get();

        return getJwtTokenResponseIfJwtTokenIsGenerated(userEntity);
    }

    private void revokeAllTokenByUser(UserEntity user) {
        List<JwtTokenEntity> validTokens = this.jwtTokenRepository.findAllByUserId(user);
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(token -> token.setActive(Boolean.FALSE));

        jwtTokenRepository.saveAll(validTokens);
    }

    private JwtTokenEntity insertJwtToken(UserEntity user) {
        JwtTokenEntity token = new JwtTokenEntity();
        token.setId(UUID.randomUUID());
        token.setUserId(user);
        token.setActive(Boolean.TRUE);
        token.setExpiredAt(this.jwtService.getTokenExpiredDate());
        token.setToken(this.jwtService.createToken(user.getUserCode(), getUserDetailsMap(user)));
        return this.jwtTokenRepository.save(token);
    }

    private Map<String, String> getUserDetailsMap(UserEntity entity) {
        if (Objects.isNull(entity))
            return Collections.emptyMap();

        return Map.of(
                "userCode", entity.getUserCode(),
                "role", entity.getUserRole().name(),
                "firstname", entity.getFirstName(),
                "lastname", entity.getLastName()
        );
    }

    private void insertUserEntity(RegisterRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setUserCode(request.getUserCode());
        userEntity.setPassword(this.passwordEncoder.encode(request.getPassword()));
        userEntity.setUserRole(request.getUserRole());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        this.userRepository.save(userEntity);
    }
}
