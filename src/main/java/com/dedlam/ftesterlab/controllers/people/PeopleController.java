package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.auth.RegistrationService;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.dedlam.ftesterlab.auth.RegistrationResult.FAILED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService service;
  private final RegistrationService registrationService;
  private final TransactionTemplate transactionTemplate;

  public PeopleController(PeopleService service, RegistrationService registrationService, TransactionTemplate transactionTemplate) {
    this.service = service;
    this.registrationService = registrationService;
    this.transactionTemplate = transactionTemplate;
  }

  @GetMapping
  public PeopleView people() {
    var people = service.people();

    return new PeopleView(people.stream().map(PeopleController::personView).toList());
  }

  @PostMapping
  public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
    var person = request.person;
    var req = new PersonDto(person.name, person.middleName, person.lastName, person.birthday);

    UUID personId = transactionTemplate.execute(txStatus -> {
      var registrationResult = registrationService.register(request.username, request.password);
      if (registrationResult == FAILED) {
        txStatus.setRollbackOnly();
        return null;
      }
      return service.create(req);
    });

    if (personId == null) {
      return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.ok(personId.toString());
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

  public record RegistrationRequest(
    String username,
    String password,
    PersonView person
  ) {

  }
}
