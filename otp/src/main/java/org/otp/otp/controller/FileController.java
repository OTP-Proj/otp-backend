package org.otp.otp.controller;

import org.otp.otp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/v1/files")
@RestController
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file,
                                           @RequestParam("user_id") String userId) {
        fileService.upload(file, userId);
        return ResponseEntity.noContent().build();
    }
}
