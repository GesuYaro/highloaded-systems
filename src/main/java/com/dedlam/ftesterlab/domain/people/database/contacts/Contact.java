package com.dedlam.ftesterlab.domain.people.database.contacts;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "contacts")
public class Contact {
  public enum ContactType {PHONE, EMAIL, TELEGRAM, VK}

  public Contact() {
  }

  public Contact(UUID id, ContactType type, String value) {
    this.id = id;
    this.type = type;
    this.value = value;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private UUID id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "contact_type", nullable = false)
  private ContactType type;

  @Column(name = "contact_value", nullable = false)
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
