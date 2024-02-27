package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.ContactDto;
import com.dedlam.ftesterlab.domain.people.models.Contact;

import java.util.List;
import java.util.UUID;

public interface ContactsService {
  List<Contact> contacts(UUID personId);

  boolean updateContacts(UUID personId, List<ContactDto> contacts);
}
