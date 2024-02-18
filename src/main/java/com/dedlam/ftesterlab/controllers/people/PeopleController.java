package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.controllers.people.dto.PersonView;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/people")
public class PeopleController extends BaseController {
  private final PeopleService service;

  public PeopleController(UsersRepository usersRepository, PeopleService peopleService, PeopleService service) {
    super(usersRepository, peopleService);
    this.service = service;
  }

  @GetMapping("info")
  public ResponseEntity<PersonView> getInfo() {
    var user = user();
    var person = service.personByUserId(user.getId());

    if (person == null) {
      return ResponseEntity.badRequest().build();
    }

    var view = personView(person);
    return ResponseEntity.ok(view);
  }

  @PostMapping("info")
  public ResponseEntity<String> register(@RequestBody PersonView request) {
    var user = user();
    var createDto = new PersonDto(request.name(), request.middleName(), request.lastName(), request.birthday());

    UUID personId = service.create(user, createDto);

    if (personId == null) {
      return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.ok(personId.toString());
  }

  @PutMapping("info")
  public ResponseEntity<Boolean> updateInfo(@RequestBody PersonView person) throws AuthException {
    var userId = user().getId();
    var existingPerson = service.personByUserId(userId);
    var dto = new PersonDto(person.name(), person.middleName(), person.lastName(), person.birthday());

    boolean result = service.update(existingPerson.getId(), dto);

    return ResponseEntity.ok(result);
  }

  //  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> delete(
    @PathVariable UUID id
  ) {
    service.delete(id);

    return ResponseEntity.ok(true);
  }

  private static PersonView personView(Person person) {
    return new PersonView(person.getId().toString(), person.getName(), person.getMiddleName(), person.getLastName(), person.getBirthday());
  }
}
