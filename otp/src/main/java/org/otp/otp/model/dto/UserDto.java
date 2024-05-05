package org.otp.otp.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false)
    @LastModifiedDate
    private Date modifiedAt;

    @Column(name = "is_deleted", insertable = false)
    private Boolean deleted;

    @Column(name = "is_active", insertable = false)
    private Boolean active;
}
