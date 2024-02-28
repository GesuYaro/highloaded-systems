package com.dedlam.ftesterlab.domain.people.services;

import com.dedlam.ftesterlab.domain.people.ContactDto;
import com.dedlam.ftesterlab.domain.people.PeopleServiceClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.dedlam.ftesterlab.domain.people.models.Contact.ContactType.EMAIL;
import static com.dedlam.ftesterlab.domain.people.models.Contact.ContactType.PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RemoteContactsServiceTest {
  private final PeopleServiceClient client = mock();
  private final RemoteContactsService service = new RemoteContactsService(client);

  @AfterEach
  public void checkNoMoreInvocations() {
    verifyNoMoreInteractions(client);
  }

  @Test
  public void updateContacts() {
    var personId = UUID.randomUUID();
    var contacts = List.of(
      new ContactDto(EMAIL, "a@b.com"),
      new ContactDto(PHONE, "+7...")
    );

    assertThat(service.updateContacts(personId, contacts)).isTrue();

    verify(client).updateContacts(personId, contacts);
  }

  @Test
  public void updateContacts_falseOnFailClientResponse() {
    var personId = UUID.randomUUID();
    var contacts = List.of(
      new ContactDto(EMAIL, "a@b.com"),
      new ContactDto(PHONE, "+7...")
    );
    var request = Request.create(Request.HttpMethod.PUT, "/url", Collections.emptyMap(), "".getBytes(Charset.defaultCharset()), Charset.defaultCharset(), null);
    doThrow(new FeignException.InternalServerError("", request, null, null)).when(client).updateContacts(any(), any());

    assertThat(service.updateContacts(personId, contacts)).isFalse();

    verify(client).updateContacts(personId, contacts);
  }
}