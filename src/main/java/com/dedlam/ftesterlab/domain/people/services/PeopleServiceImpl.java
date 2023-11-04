package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.dto.PersonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PeopleServiceImpl implements PeopleService {
  private final PeopleRepository repository;
  private final Logger logger;


  PeopleServiceImpl(PeopleRepository repository, Logger logger) {
    this.repository = repository;
    this.logger = logger;
  }

  @Autowired
  public PeopleServiceImpl(PeopleRepository repository) {
    this(repository, LoggerFactory.getLogger(PeopleServiceImpl.class));
  }

  @Override
  public UUID create(PersonDto request) {
    var uuid = UUID.randomUUID();
    var entity = new Person(uuid, request.getName(), request.getMiddleName(), request.getLastName(), request.getBirthday());


    try {
      repository.save(entity);
      return uuid;
    } catch (RuntimeException e) {
      var msg = String.format("Can't create person with uuid='%s'", uuid);
      logger.warn(msg, e);
      return null;
    }
  }

  @Override
  public List<Person> people() {
    return repository.findAll();
  }

  @Override
  public boolean update(UUID id, PersonDto request) {
    var notExistingPersonMsg = String.format("Can't find person by id='%s'", id);

    try {
      boolean exists = repository.existsById(id);
      if (!exists) {
        logger.warn(notExistingPersonMsg);
        return false;
      }
    } catch (RuntimeException e) {
      logger.warn(notExistingPersonMsg, e);
      return false;
    }

    try {
      var entity = new Person(id, request.getName(), request.getMiddleName(), request.getLastName(), request.getBirthday());
      repository.save(entity);
      return true;
    } catch (RuntimeException e) {
      var msg = String.format("Can't update person with id='%s'", id);
      logger.warn(msg, e);
      return false;
    }
  }

  @Override
  public void delete(UUID id) {
    boolean exists = repository.existsById(id);
    if (exists) {
      repository.deleteById(id);
    } else {
      var msg = String.format("Person with id='%s' does not exists during delete operation", id);
      logger.warn(msg);
    }
  }

  private Page<Person> page(int pageNumber, int pageSize) {
    var pageRequest = PageRequest.of(pageNumber, pageSize);

    return repository.findAll(pageRequest);
  }

}
