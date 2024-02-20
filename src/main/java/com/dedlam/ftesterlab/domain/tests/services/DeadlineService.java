package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.tests.models.Deadline;
import com.dedlam.ftesterlab.domain.tests.services.dto.DeadlineCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeadlineService {

    Page<Deadline> deadlines(Person user, Pageable pageable);
    Deadline createDeadline(DeadlineCreateDto createDto, Person user);
}
