package com.dedlam.ftesterlab.domain.people.database.contacts;

import com.dedlam.ftesterlab.domain.people.database.Person;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "person_contacts_info")
public class PersonContactsInfo {
  public PersonContactsInfo() {
  }

  public PersonContactsInfo(UUID id, Person person, List<Contact> contacts) {
    this.id = id;
    this.person = person;
    this.contacts = contacts;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(
    cascade = CascadeType.MERGE,
    orphanRemoval = true,
    optional = false
  )
  @JoinColumn(
    name = "person_id",
    referencedColumnName = "id"
  )
  private Person person;

  @OneToMany(
    cascade = CascadeType.ALL
  )
  @JoinTable(
    name = "person_contacts_info_to_contact",
    joinColumns = @JoinColumn(name = "person_contact_info_id"),
    inverseJoinColumns = @JoinColumn(name = "contact_id")
  )
  private List<Contact> contacts;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public List<Contact> getContacts() {
    return contacts;
  }

  public void setContacts(List<Contact> contacts) {
    this.contacts = contacts;
  }
}
