package org.otp.otp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.otp.otp.exception.FileRelatedException;
import org.otp.otp.model.dto.FileStorageResponse;
import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;
import org.otp.otp.repository.PersonRepository;
import org.otp.otp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final Path fileStorageLocation;
    private static final String DATA = "data:";
    private static final String BASE_64 = ";base64,";

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             RedisTemplate<String, String> redisTemplate,
                             Path fileStorageLocation) {
        this.personRepository = personRepository;
        this.redisTemplate = redisTemplate;
        this.fileStorageLocation = fileStorageLocation;
    }

    @Override
    public PersonResponse create(PersonRequest personRequest) {
        return this.personRepository.createPerson(personRequest);
    }

    @Override
    public List<PersonResponse> getPersons() {
        List<PersonResponse> personResponses = this.personRepository.getPersons();

        for (PersonResponse personResponse : personResponses) {
            FileStorageResponse response = readFileFromFileStorage(personResponse.getId());
            if (response != null && response.getFileEncodedContent() != null) {
                personResponse.setImage(response.getFileEncodedContent());
            }
        }

        return personResponses;
    }

    public FileStorageResponse readFileFromFileStorage(String userId) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String encodedContent = valueOps.get(userId);

        if (encodedContent != null) {
            return new FileStorageResponse(null, encodedContent);
        } else {
            Path userFilePath = findUserFile(userId);

            if (userFilePath == null) {
                return null;
            }

            try {
                byte[] fileBytes = Files.readAllBytes(userFilePath);
                String base64Content = DATA + Files.probeContentType(userFilePath) + BASE_64 + Base64.getEncoder().encodeToString(fileBytes);

                // Store the base64 encoded content in Redis
                valueOps.set(userId, base64Content);

                return new FileStorageResponse(userFilePath.toString(), base64Content);
            } catch (IOException ex) {
                log.error("Could not read file for user {}. Please try again!", userId, ex);
                throw new FileRelatedException("Could not read file for user " + userId + ". Please try again!");
            }
        }
    }

    private Path findUserFile(String userId) {
        try (Stream<Path> files = Files.list(fileStorageLocation)) {
            return files.filter(path -> path.getFileName().toString().startsWith(userId + "_"))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            log.error("Could not list files for user {}", userId, e);
            return null;
        }
    }

    @Override
    public PersonResponse getPersonById(String id) {
        PersonResponse personResponse = this.personRepository.getPersonById(id);
        FileStorageResponse response = readFileFromFileStorage(personResponse.getId());
        if (response != null && response.getFileEncodedContent() != null) {
            personResponse.setImage(response.getFileEncodedContent());
        }
        return personResponse;
    }

    @Override
    public PersonResponse update(String id, PersonRequest request) {
        return this.personRepository.update(id, request);
    }

    @Override
    public void delete(String id) {
        this.personRepository.delete(id);
    }
}
