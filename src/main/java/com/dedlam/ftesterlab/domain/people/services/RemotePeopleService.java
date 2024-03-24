package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.PeopleServiceClient;
import com.dedlam.ftesterlab.domain.people.models.Person;
import feign.FeignException;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RemotePeopleService implements PeopleService {
  private final PeopleServiceClient peopleServiceClient;

  public RemotePeopleService(PeopleServiceClient peopleServiceClient) {
    this.peopleServiceClient = peopleServiceClient;
  }

  @Override
  public @Nullable UUID create(UUID userId, PersonDto person) {
    var request = new CreatePersonRequest(userId, person);

    try {
      return peopleServiceClient.createPerson(request);
    } catch (FeignException e) {
      return null;
    }
  }

  @Override
  public @Nullable Person person(UUID id) {
    try {
      return peopleServiceClient.getPersonInfo(id);
    } catch (FeignException.NotFound e) {
      return null;
    }
  }

  @Override
  public PeopleResponse people(int pageNumber) {
    return peopleServiceClient.getPeople(pageNumber);
  }

  @Override
  public @Nullable Person personByUserId(UUID userId) {
    try {
      return peopleServiceClient.getPersonInfoByUserId(userId);
    } catch (FeignException.NotFound e) {
      return null;
    }
  }

  @Override
  public boolean update(UUID id, PersonDto person) {
    try {
      peopleServiceClient.updatePerson(id, person);
      return true;
    }catch (FeignException e) {
      return false;
    }
  }
}
