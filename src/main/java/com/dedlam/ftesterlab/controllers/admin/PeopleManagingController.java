package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.domain.PageRequest;
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

  private final PeopleRepository peopleRepository;

  public PeopleManagingController(PeopleRepository peopleRepository) {
    this.peopleRepository = peopleRepository;
  }

  @GetMapping("people")
  public ResponseEntity<PeopleView> people(@RequestParam int pageNumber) {
    var pageRequest = PageRequest.of(pageNumber, 50);
    var result = peopleRepository.findAll(pageRequest);
    var people = new PeopleView(
      result.getTotalPages(),
      result.toList().stream().map(PeopleManagingController::personView).toList()
    );

    return ResponseEntity.ok(people);
  }

  private static PersonView personView(Person person) {
    return new PersonView(
      person.getUser().getUsername(), person.getId().toString(), person.getName(),
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
