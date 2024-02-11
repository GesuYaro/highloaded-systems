package com.dedlam.ftesterlab.auth;

import com.dedlam.ftesterlab.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<User, UUID> {
  User findByUsername(String username);
}
