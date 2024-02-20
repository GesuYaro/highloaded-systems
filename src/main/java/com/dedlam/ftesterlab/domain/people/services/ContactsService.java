package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;

import java.util.List;
import java.util.UUID;

public interface ContactsService {
  boolean updateContacts(UUID personId, List<Contact> contacts);
}
