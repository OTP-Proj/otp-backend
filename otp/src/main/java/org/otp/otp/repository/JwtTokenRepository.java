package org.otp.otp.repository;

import org.otp.otp.model.entity.JwtTokenEntity;
import org.otp.otp.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, UUID> {

    List<JwtTokenEntity> findAllByUserId(UserEntity user);
}
