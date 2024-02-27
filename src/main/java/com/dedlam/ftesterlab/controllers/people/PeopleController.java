package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.auth.AuthService;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.controllers.people.dto.PersonView;
import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.people.services.PersonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/people")
public class PeopleController extends BaseController {
  private static final Logger logger = LoggerFactory.getLogger(PeopleController.class);

  private final PeopleService service;

  public PeopleController(PeopleService peopleService, AuthService authService, PeopleService service) {
    super(peopleService, authService);
    this.service = service;
  }

  @GetMapping("info")
  public ResponseEntity<?> getInfo() {
    var user = user();
    var person = service.personByUserId(user.id());

    if (person == null) {
      var message = String.format("Can't find person-info for user '%s' with id='%s'", user.username(), user.id());
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var view = personView(person);
    return ResponseEntity.ok(view);
  }

  @PostMapping("info")
  public ResponseEntity<String> register(@RequestBody PersonView request) {
    var user = user();
    var createDto = new PersonDto(request.name(), request.middleName(), request.lastName(), request.birthday());

    var existingPerson = person();
    if (existingPerson != null) {
      var message = String.format("Can't add person info for user '%s', because it is already added", user.username());
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

//    UUID personId = service.create(user, createDto); //todo
    UUID personId = null;
    if (personId == null) {
      return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.ok(personId.toString());
  }

  @PutMapping("info")
  public ResponseEntity<?> updateInfo(@RequestBody PersonView person) {
    var existingPerson = person();

    if (existingPerson == null) {
      var user = user();
      var message = String.format(
        "Can't update person, because no person-info for user '%s' with id='%s'",
        user.username(), user.id()
      );
      logger.warn(message);
      return new ResponseEntity<>(message, UNPROCESSABLE_ENTITY);
    }

    var dto = new PersonDto(person.name(), person.middleName(), person.lastName(), person.birthday());

    boolean result = service.update(existingPerson.getId(), dto);

    return ResponseEntity.ok(result);
  }

  private static PersonView personView(Person person) {
    return new PersonView(person.getId().toString(), person.getName(), person.getMiddleName(), person.getLastName(), person.getBirthday());
  }
}
