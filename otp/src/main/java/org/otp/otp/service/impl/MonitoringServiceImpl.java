package org.otp.otp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.otp.otp.model.dto.MonitoringResponse;
import org.otp.otp.repository.MonitoringRepository;
import org.otp.otp.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MonitoringServiceImpl implements MonitoringService {
    private final MonitoringRepository monitoringRepository;

    @Autowired
    public MonitoringServiceImpl(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Override
    public List<MonitoringResponse> live() {
        return monitoringRepository.getLiveData();
    }

    @Override
    public List<MonitoringResponse> offline(String username, String from, String to, String cardId, String roomNumber) {
        return monitoringRepository.getFilteredData(username, from, to, cardId, roomNumber);
    }
}
