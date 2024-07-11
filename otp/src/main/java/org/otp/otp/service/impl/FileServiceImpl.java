package org.otp.otp.service.impl;

import org.otp.otp.model.dto.FileStorageResponse;
import org.otp.otp.repository.FileRepository;
import org.otp.otp.service.FileService;
import org.otp.otp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository,
                           FileStorageService fileStorageService,
                           RedisTemplate<String, String> redisTemplate) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void upload(MultipartFile file, String userId) {
        FileStorageResponse fileStorageResponse = this.fileStorageService.writeFileToFileStorage(file, userId);

        this.fileRepository.updateFilePath(userId, fileStorageResponse.getFilePath());

        insertFileEncodedContentIntoRedis(userId, fileStorageResponse.getFileEncodedContent());
    }

    private void insertFileEncodedContentIntoRedis(String userId, String fileEncodedContent) {
        redisTemplate.opsForValue().set(userId, fileEncodedContent, 1, TimeUnit.DAYS);
    }
}
