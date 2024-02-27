package com.dedlam.ftesterlab.controllers;

import com.dedlam.ftesterlab.auth.models.User;
import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.users.UserService;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class BaseController {
  private final UserService userService;
  private final PeopleService peopleService;

  public BaseController(UserService userService, PeopleService peopleService) {
    this.userService = userService;
    this.peopleService = peopleService;
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

  protected String username() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  protected User user() {
    String username = username();
    return userService.findUserByUsername(username);
  }

  protected @Nullable Person person() {
    var user = user();
    return peopleService.personByUserId(user.id());
  }
}
