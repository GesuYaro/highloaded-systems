package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.tests.services.dto.SubjectCreateDto;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import jakarta.security.auth.message.AuthException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SubjectService {

    Subject create(SubjectCreateDto createDto, String username) throws AuthException;
    Page<Subject> subjects(String username, Pageable pageable) throws AuthException;
}
