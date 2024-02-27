package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.auth.models.DefaultUser;
import com.dedlam.ftesterlab.domain.people.database.PeopleRepository;
import com.dedlam.ftesterlab.domain.people.models.Person;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PeopleServiceImplTest {
  private final Logger logger = mock(Logger.class);
  private final PeopleRepository repository = mock(PeopleRepository.class);
  private final ContactsService contactsService = mock(ContactsService.class);
  private final PeopleServiceImpl service = new PeopleServiceImpl(repository, contactsService, logger);

  @BeforeEach
  public void setUp() {

  }

  @AfterEach
  public void checkNoMoreInvocations() {
    verifyNoMoreInteractions(repository, contactsService, logger);
  }

  @Test
  public void create() {
    var user = new DefaultUser(UUID.randomUUID(), "username", "password");
    var createDto = new PersonDto("Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"));
    var expectedPersonToSave = new Person(null, "Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"), user);
    var expectedSavedPersonId = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var expectedSavedPerson = createPerson(expectedSavedPersonId, user);
    var personToSaveCaptor = ArgumentCaptor.forClass(Person.class);
    when(repository.save(any())).thenReturn(expectedSavedPerson);

    assertThat(service.create(user, createDto)).isEqualTo(expectedSavedPersonId);

    verify(repository).save(personToSaveCaptor.capture());
    verify(contactsService).updateContacts(expectedSavedPersonId, Collections.emptyList());
    assertThat(personToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedPersonToSave);
  }

  @Test
  public void create_exceptionWhenSavePerson() {
    var user = new DefaultUser(UUID.randomUUID(), "username", "password");
    var createDto = new PersonDto("Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"));
    var expectedPersonToSave = new Person(null, "Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"), user);
    var personToSaveCaptor = ArgumentCaptor.forClass(Person.class);
    var expectedException = new RuntimeException("EXCEPTION MSG");
    when(repository.save(any())).thenThrow(expectedException);

    assertThat(service.create(user, createDto)).isNull();

    verify(repository).save(personToSaveCaptor.capture());
    verify(logger).warn("Can't create person", expectedException);
    assertThat(personToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedPersonToSave);
  }

  @Test
  public void person() {
    var id = UUID.randomUUID();
    var expected = new Person();
    when(repository.findById(any())).thenReturn(Optional.of(expected));

    assertThat(service.person(id)).isSameAs(expected);
    verify(repository).findById(id);
  }

  @Test
  public void person_notFound() {
    var id = UUID.randomUUID();
    when(repository.findById(any())).thenReturn(Optional.empty());

    assertThat(service.person(id)).isNull();
    verify(repository).findById(id);
  }

  @Test
  public void personByUserId() {
    var id = UUID.randomUUID();
    var expected = new Person();
    when(repository.findByUserId(any())).thenReturn(Optional.of(expected));

    assertThat(service.personByUserId(id)).isSameAs(expected);
    verify(repository).findByUserId(id);
  }

  @Test
  public void personByUserId_notFound() {
    var id = UUID.randomUUID();
    when(repository.findByUserId(any())).thenReturn(Optional.empty());

    assertThat(service.personByUserId(id)).isNull();
    verify(repository).findByUserId(id);
  }

  @Test
  public void update() {
    var id = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var userBoundToExistingPerson = new DefaultUser(UUID.randomUUID(), "username", "password");
    var existingPerson = createPerson(id, userBoundToExistingPerson);
    var updateDto = new PersonDto("Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"));
    var expectedPersonToSave = new Person(id, "Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"), userBoundToExistingPerson);
    var personToSaveCaptor = ArgumentCaptor.forClass(Person.class);
    when(repository.findById(any())).thenReturn(Optional.of(existingPerson));

    assertThat(service.update(id, updateDto)).isTrue();

    verify(repository).findById(id);
    verify(repository).save(personToSaveCaptor.capture());
    assertThat(personToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedPersonToSave);
  }

  @Test
  public void update_cantFindPerson() {
    var id = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var updateDto = new PersonDto("Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"));
    when(repository.findById(any())).thenReturn(Optional.empty());

    assertThat(service.update(id, updateDto)).isFalse();

    verify(repository).findById(id);
    verify(logger).warn("Can't find person by id='998ee431-0b92-48f4-a0dd-56ae3edbeb5d'");
  }

  @Test
  public void update_cantUpdatePerson() {
    var id = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var userBoundToExistingPerson = new DefaultUser(UUID.randomUUID(), "username", "password");
    var existingPerson = createPerson(id, userBoundToExistingPerson);
    var updateDto = new PersonDto("Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"));
    var expectedPersonToSave = new Person(id, "Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"), userBoundToExistingPerson);
    var personToSaveCaptor = ArgumentCaptor.forClass(Person.class);
    var exceptionOnUpdate = new RuntimeException("Exception message");
    when(repository.findById(any())).thenReturn(Optional.of(existingPerson));
    when(repository.save(any())).thenThrow(exceptionOnUpdate);

    assertThat(service.update(id, updateDto)).isFalse();

    verify(repository).findById(id);
    verify(repository).save(personToSaveCaptor.capture());
    verify(logger).warn("Can't update person with id='998ee431-0b92-48f4-a0dd-56ae3edbeb5d'", exceptionOnUpdate);
    assertThat(personToSaveCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedPersonToSave);
  }

  private Person createPerson(UUID id, @Nullable DefaultUser user) {
    return new Person(
      id,
      "Name " + id, "Middle name " + id, "Last name " + id,
      LocalDate.parse("2023-12-12"),
      user
    );
  }
}