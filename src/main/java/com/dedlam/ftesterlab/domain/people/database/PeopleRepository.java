package com.dedlam.ftesterlab.domain.people.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PeopleRepository extends JpaRepository<Person, UUID> {
  Optional<Person> findByUserId(UUID userId);

  List<Person> findAllByUserUsernameIn(Set<String> usernames);
}
