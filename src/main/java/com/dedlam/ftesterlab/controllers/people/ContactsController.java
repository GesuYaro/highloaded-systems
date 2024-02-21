package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact.ContactType;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfoRepository;
import com.dedlam.ftesterlab.domain.people.services.ContactsService;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("people")
public class ContactsController extends BaseController {
  private final Logger logger;
  private final ContactsService contactsService;
  private final PersonContactsInfoRepository contactsInfoRepository;

  public ContactsController(
    UsersRepository usersRepository,
    PeopleService peopleService,
    ContactsService contactsService,
    PersonContactsInfoRepository contactsInfoRepository,
    Logger logger
  ) {
    super(usersRepository, peopleService);
    this.contactsService = contactsService;
    this.contactsInfoRepository = contactsInfoRepository;
    this.logger = logger;
  }

  @Autowired
  public ContactsController(
    UsersRepository usersRepository,
    PeopleService peopleService,
    ContactsService contactsService,
    PersonContactsInfoRepository contactsInfoRepository
  ) {
    this(usersRepository, peopleService, contactsService, contactsInfoRepository, LoggerFactory.getLogger(ContactsController.class));
  }

  @GetMapping("/contacts")
  public ResponseEntity<?> contacts() {
    var person = person();
    if (person == null) {
      var msg = composeNotExistencePersonMsg();
      logger.warn(msg);
      return new ResponseEntity<>(msg, UNPROCESSABLE_ENTITY);
    }

    var personId = person.getId();

    var contacts = contactsInfoRepository.findByPerson_Id(personId).getContacts()
      .stream()
      .map(ContactsController::contactView)
      .toList();

    return ResponseEntity.ok(contacts);
  }

  @PutMapping("/contacts")
  public ResponseEntity<?> updateContacts(@RequestBody List<ContactView> contacts) {
    var person = person();
    if (person == null) {
      var msg = composeNotExistencePersonMsg();
      logger.warn(msg);
      return new ResponseEntity<>(msg, UNPROCESSABLE_ENTITY);
    }

    var personId = person.getId();
    List<Contact> contactsReq = contacts.stream().map(c -> new Contact(null, c.type, c.value)).toList();

    contactsService.updateContacts(personId, contactsReq);

    return ResponseEntity.ok(null);
  }

  private String composeNotExistencePersonMsg() {
    var user = user();
    return String.format(
      "Can't get contacts info, because no person-info for user '%s' with id='%s'",
      user.getUsername(), user.getId()
    );
  }

  private static ContactView contactView(Contact contact) {
    return new ContactView(contact.getType(), contact.getValue());
  }

  public record ContactView(ContactType type, String value) {
  }
}
