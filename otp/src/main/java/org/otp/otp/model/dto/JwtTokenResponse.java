package org.otp.otp.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenResponse {
    private String jwt;
}
