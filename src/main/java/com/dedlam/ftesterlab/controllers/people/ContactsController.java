package com.dedlam.ftesterlab.controllers.people;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact.ContactType;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfoRepository;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("people")
public class ContactsController extends BaseController {
  private final PeopleService peopleService;
  private final PersonContactsInfoRepository contactsInfoRepository;

  public ContactsController(UsersRepository usersRepository, PeopleService peopleService, PeopleService peopleService1, PersonContactsInfoRepository contactsInfoRepository) {
    super(usersRepository, peopleService);
    this.peopleService = peopleService1;
    this.contactsInfoRepository = contactsInfoRepository;
  }

  @GetMapping("/contacts")
  public List<ContactView> contacts() {
    var personId = person().getId();

    return contactsInfoRepository.findByPerson_Id(personId).getContacts()
      .stream()
      .map(ContactsController::contactView)
      .toList();
  }

  @PostMapping("/contacts")
  public void addToPerson(@RequestBody List<ContactView> contacts) {
    var personId = person().getId();
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
