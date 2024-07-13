package org.otp.otp.service;

import org.otp.otp.model.dto.FileStorageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void upload(MultipartFile file, String userId);

    FileStorageResponse readFileFromFileStorage(String userId);
}
