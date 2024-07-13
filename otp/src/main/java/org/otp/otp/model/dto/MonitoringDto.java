package org.otp.otp.model.dto;

import lombok.Data;

@Data
public class MonitoringDto {
    private String time;
    private String device;
    private String person;
    private String type;
    private String roomNumber;
    private String image;
}
