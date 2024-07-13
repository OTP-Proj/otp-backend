package org.otp.otp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.otp.otp.exception.FileRelatedException;
import org.otp.otp.model.dto.FileStorageResponse;
import org.otp.otp.repository.FileRepository;
import org.otp.otp.service.FileService;
import org.otp.otp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final RedisTemplate<String, String> redisTemplate;
    private final Path fileStorageLocation;
    private static final String DATA = "data:";
    private static final String BASE_64 = ";base64,";

    @Autowired
    public FileServiceImpl(FileRepository fileRepository,
                           FileStorageService fileStorageService,
                           RedisTemplate<String, String> redisTemplate,
                           Path fileStorageLocation) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
        this.redisTemplate = redisTemplate;
        this.fileStorageLocation = fileStorageLocation;
    }

    @Override
    public void upload(MultipartFile file, String userId) {
        FileStorageResponse fileStorageResponse = this.fileStorageService.writeFileToFileStorage(file, userId);

        this.fileRepository.updateFilePath(userId, fileStorageResponse.getFilePath());

        insertFileEncodedContentIntoRedis(fileStorageResponse);
    }

    @Override
    public FileStorageResponse readFileFromFileStorage(String imagePath) {
        if (StringUtils.isEmpty(imagePath)) {
            return null;
        }
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String encodedContent = valueOps.get(imagePath);

        if (encodedContent != null) {
            return new FileStorageResponse(null, encodedContent);
        } else {
            Path userFilePath = findUserFile(imagePath);

            if (userFilePath == null) {
                return null;
            }

            try {
                byte[] fileBytes = Files.readAllBytes(userFilePath);
                String base64Content = DATA + Files.probeContentType(userFilePath) + BASE_64 + Base64.getEncoder().encodeToString(fileBytes);

                // Store the base64 encoded content in Redis
                valueOps.set(imagePath, base64Content);

                return new FileStorageResponse(userFilePath.toString(), base64Content);
            } catch (IOException ex) {
                log.error("Could not read file for user {}. Please try again!", imagePath, ex);
                throw new FileRelatedException("Could not read file for user " + imagePath + ". Please try again!");
            }
        }
    }

    private Path findUserFile(String imagePath) {
        Path path = Paths.get(fileStorageLocation.toString(), imagePath);
        if (Files.exists(path)) {
            return path;
        } else {
            return null;
        }
    }

    private void insertFileEncodedContentIntoRedis(FileStorageResponse fileStorageResponse) {
        redisTemplate.opsForValue().set(fileStorageResponse.getFilePath(), fileStorageResponse.getFileEncodedContent(),
                1, TimeUnit.DAYS);
    }


}
