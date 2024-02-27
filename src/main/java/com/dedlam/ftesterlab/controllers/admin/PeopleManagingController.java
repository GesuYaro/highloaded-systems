package com.dedlam.ftesterlab.controllers.admin;

import com.dedlam.ftesterlab.auth.AuthService;
import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import com.dedlam.ftesterlab.utils.exceptions.ExceptionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class PeopleManagingController {
  private final AuthService authService;
  private final PeopleService peopleService;
  private final CircuitBreakerFactory circuitBreakerFactory;

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
    var cb = circuitBreakerFactory.create("cb");
    var username = cb.run(
            () -> authService.user(person.getId()).username(),
            throwable -> { throw new BaseException(ExceptionType.BAD_REQUEST); }
    );
    return new PersonView(
      username, person.getId().toString(), person.getName(),
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

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<String> handleBaseException(BaseException e) {
    HttpStatus status = null;
    switch (e.getExceptionType()) {
      case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
      case FORBIDDEN -> status = HttpStatus.FORBIDDEN;
      case BAD_REQUEST -> status = HttpStatus.BAD_REQUEST;
    }
    return new ResponseEntity<>(e.getMessage(), status);
  }
}
