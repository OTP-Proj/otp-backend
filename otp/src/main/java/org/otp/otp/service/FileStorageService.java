package org.otp.otp.service;

import org.otp.otp.model.dto.FileStorageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileStorageResponse writeFileToFileStorage(MultipartFile file, String userId);

}
