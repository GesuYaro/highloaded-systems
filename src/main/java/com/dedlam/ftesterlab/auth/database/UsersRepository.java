package com.dedlam.ftesterlab.auth.database;

import com.dedlam.ftesterlab.auth.models.DefaultUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<DefaultUser, UUID> {
  Optional<DefaultUser> findUserByUsername(String username);
}
