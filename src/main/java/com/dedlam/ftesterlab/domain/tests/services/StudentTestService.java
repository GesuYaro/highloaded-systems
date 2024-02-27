package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.tests.models.Deadline;
import com.dedlam.ftesterlab.domain.tests.models.TestResult;
import com.dedlam.ftesterlab.domain.tests.services.dto.StartTestDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestSubmitDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestWithTestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentTestService {

    Page<Deadline> incomingDeadlines(Person user, Pageable pageable);
    TestWithTestResult startTest(StartTestDto startTestDto, Person user);
    TestResult submitTest(TestSubmitDto submitDto, Person user);
}
