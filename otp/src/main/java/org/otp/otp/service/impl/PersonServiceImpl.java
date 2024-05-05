package org.otp.otp.service.impl;

import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;
import org.otp.otp.repository.PersonRepository;
import org.otp.otp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonResponse create(PersonRequest personRequest) {
        return this.personRepository.createPerson(personRequest);
    }

    @Override
    public List<PersonResponse> getPersons() {
        return this.personRepository.getPersons();
    }

    @Override
    public PersonResponse getPersonById(String id) {
        return this.personRepository.getPersonById(id);
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
