package com.dedlam.ftesterlab.domain.people.models;

import java.util.UUID;

public class Contact {
  public enum ContactType {PHONE, EMAIL, TELEGRAM, VK}

  public Contact() {
  }

  public Contact(UUID id, ContactType type, String value) {
    this.id = id;
    this.type = type;
    this.value = value;
  }

  private UUID id;

  private ContactType type;

  private String value;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ContactType getType() {
    return type;
  }

  public void setType(ContactType type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
