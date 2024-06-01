package org.otp.otp.service;

import org.otp.otp.model.dto.MonitoringResponse;

import java.util.List;

public interface MonitoringService {
    List<MonitoringResponse> live(boolean isInside);

    List<MonitoringResponse> offline(String username, String from, String to, String cardId, String roomNumber);
}
