package org.otp.otp.controller;

import org.otp.otp.model.dto.PersonRequest;
import org.otp.otp.model.dto.PersonResponse;
import org.otp.otp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody PersonRequest personRequest) {
//        return ResponseEntity.ok(this.personService.create(personRequest));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getPersons() {
        return ResponseEntity.ok(this.personService.getPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable String id) {
        return ResponseEntity.ok(this.personService.getPersonById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePersonById(@PathVariable String id,
                                                           @RequestBody PersonRequest request) {
        this.personService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonById(@PathVariable String id) {
        this.personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
