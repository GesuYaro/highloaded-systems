package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact.ContactType;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfoRepository;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("people")
public class ContactsController {
  private final PeopleService peopleService;
  private final PersonContactsInfoRepository contactsInfoRepository;

  @Autowired
  public ContactsController(PeopleService peopleService, PersonContactsInfoRepository contactsInfoRepository) {
    this.peopleService = peopleService;
    this.contactsInfoRepository = contactsInfoRepository;
  }

  @GetMapping("/{personId}/contacts")
  public List<ContactView> contacts(@PathVariable UUID personId) {
    return contactsInfoRepository.findByPerson_Id(personId).getContacts()
      .stream()
      .map(ContactsController::contactView)
      .toList();
  }

  @PostMapping("/{personId}/contacts")
  public void addToPerson(@PathVariable UUID personId, @RequestBody List<ContactView> contacts) {
    List<Contact> contactsReq = contacts.stream().map(c -> new Contact(null, c.type, c.value)).toList();

    peopleService.bindContacts(personId, contactsReq);

    return;
  }

  private static ContactView contactView(Contact contact) {
    return new ContactView(contact.getType(), contact.getValue());
  }

  public record ContactView(ContactType type, String value) {
  }
}
