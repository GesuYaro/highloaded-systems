package com.dedlam.ftesterlab.controllers;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.auth.models.DefaultUser;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import jakarta.annotation.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {
  private final UsersRepository usersRepository;
  private final PeopleService peopleService;

  public BaseController(UsersRepository usersRepository, PeopleService peopleService) {
    this.usersRepository = usersRepository;
    this.peopleService = peopleService;
  }

  protected String username() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  protected DefaultUser user() {
    String username = username();
    return usersRepository.findUserByUsername(username).orElse(null);
  }

  protected @Nullable Person person() {
    var user = user();
    return peopleService.personByUserId(user.getId());
  }
}
