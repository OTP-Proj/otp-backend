package org.otp.otp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.otp.otp.model.dto.FileStorageResponse;
import org.otp.otp.model.dto.MonitoringDto;
import org.otp.otp.model.dto.MonitoringResponse;
import org.otp.otp.repository.MonitoringRepository;
import org.otp.otp.service.FileService;
import org.otp.otp.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MonitoringServiceImpl implements MonitoringService {
    private final MonitoringRepository monitoringRepository;
    private final FileService fileService;

    @Autowired
    public MonitoringServiceImpl(MonitoringRepository monitoringRepository,
                                 FileService fileService) {
        this.monitoringRepository = monitoringRepository;
        this.fileService = fileService;
    }

    @Override
    public List<MonitoringResponse> live(boolean isInside) {
        List<MonitoringDto> monitoringDTOs = monitoringRepository.getLiveData(isInside);
        List<MonitoringResponse> monitoringResponses = new ArrayList<>();
        for (MonitoringDto monitoringDto : monitoringDTOs) {
            FileStorageResponse response = fileService.readFileFromFileStorage(monitoringDto.getImage());

            MonitoringResponse monitoringResponse = map(monitoringDto);

            if (response != null && response.getFileEncodedContent() != null) {
                monitoringResponse.setImage(response.getFileEncodedContent());
            }

            monitoringResponses.add(monitoringResponse);
        }
        return monitoringResponses;
    }


    @Override
    public List<MonitoringResponse> offline(String username, String from, String to, String cardId, String roomNumber) {
        List<MonitoringDto> monitoringDTOs = monitoringRepository.getFilteredData(username, from, to, cardId, roomNumber);
        List<MonitoringResponse> monitoringResponses = new ArrayList<>();

        for (MonitoringDto monitoringDto : monitoringDTOs) {
            FileStorageResponse response = fileService.readFileFromFileStorage(monitoringDto.getImage());

            MonitoringResponse monitoringResponse = map(monitoringDto);

            if (response != null && response.getFileEncodedContent() != null) {
                monitoringResponse.setImage(response.getFileEncodedContent());
            }

            monitoringResponses.add(monitoringResponse);
        }
        return monitoringResponses;
    }

    private MonitoringResponse map(MonitoringDto monitoringDto) {
        MonitoringResponse monitoringResponse = new MonitoringResponse();
        monitoringResponse.setType(monitoringDto.getType());
        monitoringResponse.setTime(monitoringDto.getTime());
        monitoringResponse.setDevice(monitoringDto.getDevice());
        monitoringResponse.setPerson(monitoringDto.getPerson());
        monitoringResponse.setRoomNumber(monitoringDto.getRoomNumber());
        return monitoringResponse;
    }
}
