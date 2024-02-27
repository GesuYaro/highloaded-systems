package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.ContactDto;
import com.dedlam.ftesterlab.domain.people.PeopleServiceClient;
import com.dedlam.ftesterlab.domain.people.models.Contact;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RemoteContactsService implements ContactsService {
  private final PeopleServiceClient peopleServiceClient;

  public RemoteContactsService(PeopleServiceClient peopleServiceClient) {
    this.peopleServiceClient = peopleServiceClient;
  }

  @Override
  public List<Contact> contacts(UUID personId) {
    return peopleServiceClient.getContacts(personId);
  }

  @Override
  public boolean updateContacts(UUID personId, List<ContactDto> contacts) {
    peopleServiceClient.updateContacts(personId, contacts);

    return true;
  }
}
