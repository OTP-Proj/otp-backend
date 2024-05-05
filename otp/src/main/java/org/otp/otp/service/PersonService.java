package org.otp.otp.service;

import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;

import java.util.List;

public interface PersonService {

    PersonResponse create(PersonRequest personRequest);

    List<PersonResponse> getPersons();

    PersonResponse getPersonById(String id);

    PersonResponse update(String id, PersonRequest request);

    void delete(String id);
}
