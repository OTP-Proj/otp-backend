package org.otp.otp.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Data
public class PersonResponse {
    private String id;
    private String username;
    private String surname;
    private String roomNumber;
    private String cardId;
    private String userType;

    private String createdAt;

    private String modifiedAt;

    private Boolean active;
}
