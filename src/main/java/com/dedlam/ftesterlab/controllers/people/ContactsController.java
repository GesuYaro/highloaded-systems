package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact.ContactType;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfoRepository;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("people")
public class ContactsController {
  private final PeopleService peopleService;
  private final PersonContactsInfoRepository contactsInfoRepository;
  private final UsersRepository usersRepository;

  public ContactsController(PeopleService peopleService, PersonContactsInfoRepository contactsInfoRepository, UsersRepository usersRepository) {
    this.peopleService = peopleService;
    this.contactsInfoRepository = contactsInfoRepository;
    this.usersRepository = usersRepository;
  }

  @GetMapping("/contacts")
  public List<ContactView> contacts() throws AuthException {
    var personId = personId();

    return contactsInfoRepository.findByPerson_Id(personId).getContacts()
      .stream()
      .map(ContactsController::contactView)
      .toList();
  }

  @PostMapping("/contacts")
  public void addToPerson(@RequestBody List<ContactView> contacts) throws AuthException {
    var personId = personId();
    List<Contact> contactsReq = contacts.stream().map(c -> new Contact(null, c.type, c.value)).toList();

    peopleService.bindContacts(personId, contactsReq);

    return;
  }

  private UUID personId() throws AuthException {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var userDetails = usersRepository.findUserByUsername(username).orElseThrow(() -> new AuthException("Can't find user"));
    return peopleService.personByUserId(userDetails.getId()).getId();
  }

  private static ContactView contactView(Contact contact) {
    return new ContactView(contact.getType(), contact.getValue());
  }

  public record ContactView(ContactType type, String value) {
  }
}
