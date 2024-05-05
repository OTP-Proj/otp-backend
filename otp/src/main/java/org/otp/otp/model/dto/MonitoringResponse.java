package org.otp.otp.model.dto;

import lombok.Data;

@Data
public class MonitoringResponse {
    private String time;
    private String device;
    private String person;
    private String type;
    private String card;
}
