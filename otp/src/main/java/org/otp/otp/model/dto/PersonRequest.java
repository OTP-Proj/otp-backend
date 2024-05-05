package org.otp.otp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonRequest {
    private String username;
    private String surname;
    private String roomNumber;
    private String cardId;
    private UserType userType;
}
