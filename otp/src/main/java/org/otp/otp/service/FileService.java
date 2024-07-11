package org.otp.otp.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void upload(MultipartFile file, String userId);
}
