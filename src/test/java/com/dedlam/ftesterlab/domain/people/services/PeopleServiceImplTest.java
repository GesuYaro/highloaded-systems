package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.PeopleServiceClient;
import com.dedlam.ftesterlab.domain.people.models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PeopleServiceImplTest {
  private final PeopleServiceClient client = mock();
  private final RemotePeopleService service = new RemotePeopleService(client);

  @BeforeEach
  public void setUp() {

  }

  @AfterEach
  public void checkNoMoreInvocations() {
    verifyNoMoreInteractions(client);
  }

  @Test
  public void create() {
    var userId = UUID.randomUUID();
    var createDto = new PersonDto("Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"));
    var expectedCreateRequest = new CreatePersonRequest(userId, createDto);
    var expectedSavedPersonId = UUID.fromString("998ee431-0b92-48f4-a0dd-56ae3edbeb5d");
    var createPersonRequestCaptor = ArgumentCaptor.forClass(CreatePersonRequest.class);
    when(client.createPerson(any())).thenReturn(expectedSavedPersonId);

    assertThat(service.create(userId, createDto)).isEqualTo(expectedSavedPersonId);

    verify(client).createPerson(createPersonRequestCaptor.capture());
    assertThat(createPersonRequestCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedCreateRequest);
  }

  @Test
  public void person() {
    var id = UUID.randomUUID();
    var expected = new Person();
    when(client.getPersonInfo(any())).thenReturn(expected);

    assertThat(service.person(id)).isSameAs(expected);
    verify(client).getPersonInfo(id);
  }

  @Test
  public void personByUserId() {
    var id = UUID.randomUUID();
    var expected = new Person();
    when(client.getPersonInfoByUserId(id)).thenReturn(expected);

    assertThat(service.personByUserId(id)).isSameAs(expected);
    verify(client).getPersonInfoByUserId(id);
  }

  @Test
  public void update() {
    var id = UUID.randomUUID();
    var dto = new PersonDto("Name", "Middle name", "Last name", LocalDate.parse("2023-12-12"));

    assertThat(service.update(id, dto)).isTrue();

    verify(client).updatePerson(id, dto);
  }
}