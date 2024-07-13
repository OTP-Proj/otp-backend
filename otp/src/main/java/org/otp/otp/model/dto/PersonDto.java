package org.otp.otp.model.dto;

import lombok.Data;

@Data
public class PersonDto {
    private String id;
    private String username;
    private String surname;
    private String roomNumber;
    private String userType;
    private String createdAt;
    private String modifiedAt;
    private Boolean active;
    private String personPin;
    private String image;
}
