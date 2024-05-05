package org.otp.otp.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "otp_db", name = "person")
public class PersonEntity {

    private String username;

    private String surname;

    private String userType;

    private String cardNumber;
    private String roomNumber;
}
