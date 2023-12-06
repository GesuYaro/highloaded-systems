package com.dedlam.ftesterlab.domain.people.database.contacts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonContactsInfoRepository extends JpaRepository<PersonContactsInfo, UUID> {
  PersonContactsInfo findByPerson_Id(UUID personId);
}
