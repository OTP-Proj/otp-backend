package org.otp.otp.controller;

import org.otp.otp.model.dto.MonitoringResponse;
import org.otp.otp.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/monitoring")
@RestController
@CrossOrigin
public class MonitoringController {

    private final MonitoringService monitoringService;

    @Autowired
    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/live")
    public ResponseEntity<List<MonitoringResponse>> live() {
        return ResponseEntity.ok(this.monitoringService.live());
    }

    @GetMapping("/offline")
    public ResponseEntity<List<MonitoringResponse>> offline(@RequestParam(value = "username", required = false) String username,
                                                      @RequestParam(value = "from", required = false) String from,
                                                      @RequestParam(value = "to", required = false) String to,
                                                      @RequestParam(value = "card_id", required = false) String cardId,
                                                      @RequestParam(value = "room_number", required = false) String roomNumber) {
        return ResponseEntity.ok(this.monitoringService.offline(username, from, to, cardId, roomNumber));
    }
}
