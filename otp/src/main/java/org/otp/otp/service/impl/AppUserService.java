package org.otp.otp.service.impl;

import lombok.RequiredArgsConstructor;
import org.otp.otp.model.entity.JwtTokenEntity;
import org.otp.otp.model.entity.UserEntity;
import org.otp.otp.repository.JwtTokenRepository;
import org.otp.otp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenRepository tokenRepository;
    @Value("${jwt.salt}")
    private String salt;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = this.userRepository.findByUserCode(userCode);

        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User '" + userCode + "' not found");
        }

        var user = userEntity.get();

        return User.builder()
                .username(user.getUserCode())
                .password(user.getPassword())
                .authorities(Collections.EMPTY_LIST)
                .build();
    }

    public List<JwtTokenEntity> getJwtTokensByUserCode(String userCode) {
        Optional<UserEntity> userEntity = this.userRepository.findByUserCode(userCode);

        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User '" + userCode + "' not found");
        }
        var user = userEntity.get();
        return tokenRepository.findAllByUserId(user);
    }
}
