package org.otp.otp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.otp.otp.exception.FileRelatedException;
import org.otp.otp.model.dto.FileStorageResponse;
import org.otp.otp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    private final String fileStoragePath;
    private final Path fileStorageLocation;
    private static final String DATA = "data:";
    private static final String BASE_64 = ";base64,";

    @Autowired
    public FileStorageServiceImpl(@Value("${filestorage.path}") String fileStoragePath) {
        this.fileStorageLocation = Paths.get(fileStoragePath)
                .toAbsolutePath().normalize();
        this.fileStoragePath = fileStoragePath;
        try {
            if (!Files.exists(this.fileStorageLocation)) {
                Files.createDirectories(this.fileStorageLocation);
            }
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new FileRelatedException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    public Path findUserFile(String imagePath) {
        Path path = Paths.get(fileStorageLocation.toString(), imagePath);
        if (Files.exists(path)) {
            return path;
        } else {
            return null;
        }
    }


    @Override
    public FileStorageResponse writeFileToFileStorage(MultipartFile file, String userId) {
        if (Objects.isNull(file.getOriginalFilename())) {
            throw new FileRelatedException();
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileRelatedException("Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(userId + "_" + fileName);

            removeUserFiles(userId);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String base64Content = DATA + file.getContentType() + BASE_64 + Base64.getEncoder().encodeToString(file.getBytes());

            return new FileStorageResponse(targetLocation.toString(), base64Content);
        } catch (IOException ex) {
            log.error(ex.toString());
            throw new FileRelatedException("Could not store file " + fileName + ". Please try again!");
        }
    }

    private void removeUserFiles(String userId) throws IOException {
        try (Stream<Path> files = Files.list(fileStorageLocation)) {
            files.filter(path -> path.getFileName().toString().startsWith(userId + "_"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.error("Could not delete file: {}", path, e);
                        }
                    });
        }
    }


}
