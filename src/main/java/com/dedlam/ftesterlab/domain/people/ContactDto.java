package com.dedlam.ftesterlab.domain.people;

import com.dedlam.ftesterlab.domain.people.models.Contact.ContactType;

public record ContactDto(
  ContactType type,
  String value
) {
}
