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
  public UUID create(UUID userId, PersonDto person) {
    var request = new CreatePersonRequest(userId, person);

    return peopleServiceClient.createPerson(request);
  }

  @Override
  public Person person(UUID id) {
    return peopleServiceClient.getPersonInfo(id);
  }

  @Override
  public PeopleResponse people(int pageNumber) {
    return peopleServiceClient.getPeople(pageNumber);
  }

  @Nullable
  @Override
  public Person personByUserId(UUID userId) {
    try {
      return peopleServiceClient.getPersonInfoByUserId(userId);
    } catch (FeignException.NotFound e) {
      return null;
    }
  }

  @Override
  public boolean update(UUID id, PersonDto person) {
    peopleServiceClient.updatePerson(id, person);

    return true;
  }
}
