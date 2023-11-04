package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService service;

  @Autowired
  public PeopleController(
    PeopleService service
  ) {
    this.service = service;
  }

  @GetMapping
  public PeopleView people() {
    var people = service.people();

    return new PeopleView(people.stream().map(PeopleController::personView).toList());
  }

  @PostMapping
  public ResponseEntity<String> create(
    @RequestBody PersonView person
  ) {
    var req = new PersonDto(person.name, person.middleName, person.lastName, person.birthday);

    UUID result = service.create(req);

    if (result == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.ok(result.toString());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Boolean> update(
    @PathVariable UUID id,
    @RequestBody PersonView person
  ) {
    var dto = new PersonDto(person.name, person.middleName, person.lastName, person.birthday);

    boolean result = service.update(id, dto);

    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> delete(
    @PathVariable UUID id
  ) {
    service.delete(id);

    return ResponseEntity.ok(true);
  }

  private static PersonView personView(Person person) {
    return new PersonView(person.getId().toString(), person.getName(), person.getMiddleName(), person.getLastName(), person.getBirthday());
  }

  public record PeopleView(List<PersonView> people) {
  }

  public record PersonView(
    String id,
    String name,
    String middleName,
    String lastName,
    @JsonFormat(pattern = "dd.MM.yyyy") LocalDate birthday
  ) {
  }
}
