package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestChangeStateDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestCreateDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestSearchDto;
import jakarta.security.auth.message.AuthException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestService {

    Page<Test> tests(TestSearchDto searchDto, Pageable pageable);
    Test createTest(TestCreateDto createDto, Person owner);
    Test changeTestOpenState(TestChangeStateDto changeStateDto, Person owner);
}
