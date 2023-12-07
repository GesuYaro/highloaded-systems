package com.dedlam.ftesterlab.domain.people.database.contacts;

import com.dedlam.ftesterlab.domain.people.database.Person;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "person_contacts_info")
public class PersonContactsInfo {
  /* TODO */ private static final String PERSON_FK_DESCRIPTION = "FOREIGN KEY (person_id) REFERENCES people(id) on update cascade on delete cascade";

  /* TODO */ private static final String JOIN_TABLE_FK_DESCRIPTION = "FOREIGN KEY (person_contact_info_id) REFERENCES person_contacts_info(id) on update cascade on delete cascade"; // TODO

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
    cascade = CascadeType.REMOVE,
    orphanRemoval = true,
    optional = false
  )
  @JoinColumn(
    name = "person_id",
    referencedColumnName = "id",
    foreignKey = @ForeignKey(foreignKeyDefinition = PERSON_FK_DESCRIPTION)
  )
  private Person person;
  @OneToMany(
    cascade = CascadeType.ALL
  )
  @JoinTable(
    name = "person_contacts_info_to_contact",
    joinColumns = @JoinColumn(name = "person_contact_info_id", foreignKey = @ForeignKey(foreignKeyDefinition = JOIN_TABLE_FK_DESCRIPTION)),
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
