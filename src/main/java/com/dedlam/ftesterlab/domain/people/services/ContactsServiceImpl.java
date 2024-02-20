package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfo;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ContactsServiceImpl implements ContactsService {
  private final PersonContactsInfoRepository contactsInfoRepository;

  public ContactsServiceImpl(PersonContactsInfoRepository contactsInfoRepository) {
    this.contactsInfoRepository = contactsInfoRepository;
  }

  @Override
  public boolean updateContacts(UUID personId, List<Contact> contacts) {
    contacts.forEach(c -> c.setId(null));

    PersonContactsInfo contactsInfo = contactsInfoRepository.findByPerson_Id(personId);
    if (contactsInfo == null) {
      var person = new Person();
      person.setId(personId);
      contactsInfo = new PersonContactsInfo(null, person, Collections.emptyList());
    }

    List<Contact> existingContacts = contactsInfo.getContacts();
    existingContacts.clear();
    existingContacts.addAll(contacts);

    contactsInfoRepository.save(contactsInfo);

    return true;
  }
}
