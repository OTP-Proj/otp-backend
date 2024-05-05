package org.otp.otp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginRequest implements Serializable {
    @NonNull
    private String userCode;
    @NonNull
    private String password;
}
