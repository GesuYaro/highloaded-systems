package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.database.contacts.Contact;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfo;
import com.dedlam.ftesterlab.domain.people.database.contacts.PersonContactsInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.dedlam.ftesterlab.TestUtils.create;
import static com.dedlam.ftesterlab.domain.people.database.contacts.Contact.ContactType.EMAIL;
import static com.dedlam.ftesterlab.domain.people.database.contacts.Contact.ContactType.PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ContactsServiceImplTest {
  private final PersonContactsInfoRepository contactsInfoRepository = mock(PersonContactsInfoRepository.class);
  private final ContactsServiceImpl service = new ContactsServiceImpl(contactsInfoRepository);

  @AfterEach
  public void checkNoMoreInvocations() {
    verifyNoMoreInteractions(contactsInfoRepository);
  }

  @Test
  public void updateContacts_noExistingContactsInfo() {
    var newContacts = List.of(
      new Contact(UUID.randomUUID(), EMAIL, "a@b.com"),
      new Contact(UUID.randomUUID(), PHONE, "+7999888...")
    );
    var personId = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var person = create(new Person(), p -> p.setId(personId));
    var expectedContactsInfoToSave = new PersonContactsInfo(null, person, newContacts);
    var contactsInfoToSaveCaptor = ArgumentCaptor.forClass(PersonContactsInfo.class);
    when(contactsInfoRepository.findByPerson_Id(any())).thenReturn(null);

    assertThat(service.updateContacts(personId, newContacts)).isTrue();

    verify(contactsInfoRepository).findByPerson_Id(personId);
    verify(contactsInfoRepository).save(contactsInfoToSaveCaptor.capture());
    assertThat(contactsInfoToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedContactsInfoToSave);
  }

  @Test
  public void updateContacts_existingContactsInfo() {
    var personId = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var person = create(new Person(), p -> p.setId(personId));
    var existingContacts = new LinkedList<>(List.of(
      new Contact(UUID.randomUUID(), EMAIL, "aaaaaaaaaaaaa@bbbbbbbbbbbbbbbb.com")
    ));
    var contactsInfoId = UUID.fromString("e9d1fe5f-92b4-4108-9b0d-42019528b6ae");
    var existingContactsInfo = new PersonContactsInfo(contactsInfoId, person, existingContacts);
    var newContacts = List.of(
      new Contact(UUID.randomUUID(), EMAIL, "a@b.com"),
      new Contact(UUID.randomUUID(), PHONE, "+7999888...")
    );
    var expectedContactsInfoToSave = new PersonContactsInfo(contactsInfoId, person, newContacts);
    var contactsInfoToSaveCaptor = ArgumentCaptor.forClass(PersonContactsInfo.class);
    when(contactsInfoRepository.findByPerson_Id(any())).thenReturn(existingContactsInfo);

    assertThat(service.updateContacts(personId, newContacts)).isTrue();

    verify(contactsInfoRepository).findByPerson_Id(personId);
    verify(contactsInfoRepository).save(contactsInfoToSaveCaptor.capture());
    assertThat(contactsInfoToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedContactsInfoToSave);
  }
}