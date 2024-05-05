package org.otp.otp.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private Date createdAt;

    private Date modifiedAt;

    private Boolean deleted;

    private Boolean active;
}
