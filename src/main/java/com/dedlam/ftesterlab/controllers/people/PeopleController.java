package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/people")
public class PeopleController {
  private final UsersRepository usersRepository;
  private final PeopleService service;

  public PeopleController(UsersRepository usersRepository, PeopleService service) {
    this.usersRepository = usersRepository;
    this.service = service;
  }

  @GetMapping("info")
  public ResponseEntity<PersonView> getInfo() throws AuthException {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var userDetails = usersRepository.findUserByUsername(username).orElseThrow(() -> new AuthException("Can't find user"));

    var userId = userDetails.getId();
    var person = service.personByUserId(userId);
    if (person == null) {
      return ResponseEntity.badRequest().build();
    }

    var view = personView(person);
    return ResponseEntity.ok(view);
  }

  @PostMapping("info")
  public ResponseEntity<String> register(@RequestBody PersonView request) throws AuthException {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var userDetails = usersRepository.findUserByUsername(username).orElseThrow(() -> new AuthException("Can't find user"));

    UUID personId = service.create(userDetails, new PersonDto(request.name, request.middleName, request.lastName, request.birthday));

    if (personId == null) {
      return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.ok(personId.toString());
  }

  @PutMapping("info")
  public ResponseEntity<Boolean> updateInfo(@RequestBody PersonView person) throws AuthException {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var userDetails = usersRepository.findUserByUsername(username).orElseThrow(() -> new AuthException("Can't find user"));

    var existingPerson = service.personByUserId(userDetails.getId());
    var dto = new PersonDto(person.name, person.middleName, person.lastName, person.birthday);

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

  public record PersonView(
    String id,
    String name,
    String middleName,
    String lastName,
    @JsonFormat(pattern = "dd.MM.yyyy") LocalDate birthday
  ) {
  }
}
