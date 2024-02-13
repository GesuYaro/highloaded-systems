package com.dedlam.ftesterlab.auth.database;

import com.dedlam.ftesterlab.auth.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminsRepository extends JpaRepository<Admin, UUID> {
  Optional<Admin> findAdminByUsername(String username);
}
