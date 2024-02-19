package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectCreateDto;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import jakarta.security.auth.message.AuthException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SubjectService {

    Subject create(SubjectCreateDto createDto, Person owner);
    Page<Subject> subjects(Person owner, Pageable pageable);
}
