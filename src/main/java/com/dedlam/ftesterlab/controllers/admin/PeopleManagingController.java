package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.AuthService;
import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("admin")
public class PeopleManagingController {
  private final AuthService authService;
  private final PeopleService peopleService;

  public PeopleManagingController(AuthService authService, PeopleService peopleService) {
    this.authService = authService;
    this.peopleService = peopleService;
  }

  @GetMapping("people")
  public ResponseEntity<PeopleView> people(@RequestParam int pageNumber) {
    var result = peopleService.people(pageNumber);

    var people = new PeopleView(
      result.totalPageNumber(),
      result.people().stream().map(this::personView).toList()
    );

    return ResponseEntity.ok(people);
  }

  private PersonView personView(Person person) {
    return new PersonView(
      authService.user(person.getId()).username(), person.getId().toString(), person.getName(),
      person.getMiddleName(), person.getLastName(), person.getBirthday()
    );
  }

  public record PeopleView(
    int totalPagesNumber,
    List<PersonView> people
  ) {
  }

  public record PersonView(
    String username,
    String id,
    String name,
    String middleName,
    String lastName,
    @JsonFormat(pattern = "dd.MM.yyyy") LocalDate birthday
  ) {
  }
}
