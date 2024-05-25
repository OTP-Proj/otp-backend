package org.otp.otp.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PersonRequest {
    private String username;
    private String surname;
    private String roomNumber;
    private String userType;
}
