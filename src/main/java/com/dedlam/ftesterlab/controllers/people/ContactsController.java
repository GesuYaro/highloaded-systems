package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.ContactDto;
import com.dedlam.ftesterlab.domain.people.models.Contact;
import com.dedlam.ftesterlab.domain.people.models.Contact.ContactType;
import com.dedlam.ftesterlab.domain.people.services.ContactsService;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("people")
public class ContactsController extends BaseController {
  private final Logger logger;
  private final ContactsService contactsService;

  public ContactsController(UserService userService, PeopleService peopleService, ContactsService contactsService) {
    super(userService, peopleService);
    this.contactsService = contactsService;
    this.logger = LoggerFactory.getLogger(ContactsController.class);
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

    var contacts = contactsService.contacts(personId)
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
    List<ContactDto> contactsReq = contacts.stream().map(c -> new ContactDto(c.type, c.value)).toList();

    contactsService.updateContacts(personId, contactsReq);

    return ResponseEntity.ok(null);
  }

  private String composeNotExistencePersonMsg() {
    var user = user();
    return String.format(
      "Can't get contacts info, because no person-info for user '%s' with id='%s'",
      user.username(), user.id()
    );
  }

  private static ContactView contactView(Contact contact) {
    return new ContactView(contact.getType(), contact.getValue());
  }

  public record ContactView(ContactType type, String value) {
  }
}
