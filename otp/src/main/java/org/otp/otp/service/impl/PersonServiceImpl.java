package org.otp.otp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.otp.otp.model.dto.FileStorageResponse;
import org.otp.otp.model.dto.PersonDto;
import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;
import org.otp.otp.repository.PersonRepository;
import org.otp.otp.service.FileService;
import org.otp.otp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final FileService fileService;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             FileService fileService) {
        this.personRepository = personRepository;
        this.fileService = fileService;
    }

    @Override
    public PersonResponse create(PersonRequest personRequest) {
        return this.personRepository.createPerson(personRequest);
    }

    @Override
    public List<PersonResponse> getPersons() {
        List<PersonDto> personDTOs = this.personRepository.getPersons();
        List<PersonResponse> personResponses = new ArrayList<>();

        for (PersonDto personDto : personDTOs) {
            PersonResponse personResponse = map(personDto);
            FileStorageResponse response = fileService.readFileFromFileStorage(personDto.getImage());
            if (response != null && response.getFileEncodedContent() != null) {
                personResponse.setImage(response.getFileEncodedContent());
            }

            personResponses.add(personResponse);
        }

        return personResponses;
    }

    @Override
    public PersonResponse getPersonById(String id) {
        PersonDto personDto = this.personRepository.getPersonById(id);
        PersonResponse personResponse = map(personDto);
        FileStorageResponse response = fileService.readFileFromFileStorage(personDto.getImage());
        if (response != null && response.getFileEncodedContent() != null) {
            personResponse.setImage(response.getFileEncodedContent());
        }
        return personResponse;
    }

    private PersonResponse map(PersonDto personDto) {
        PersonResponse personResponse = new PersonResponse();
        personResponse.setCreatedAt(personDto.getCreatedAt());
        personResponse.setPersonPin(personDto.getPersonPin());
        personResponse.setId(personDto.getId());
        personResponse.setActive(personDto.getActive());
        personResponse.setUsername(personDto.getUsername());
        personResponse.setSurname(personDto.getSurname());
        personResponse.setModifiedAt(personDto.getModifiedAt());
        personResponse.setRoomNumber(personDto.getRoomNumber());
        personResponse.setUserType(personDto.getUserType());
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
