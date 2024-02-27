package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.tests.database.DeadlineRepository;
import com.dedlam.ftesterlab.domain.tests.models.Deadline;
import com.dedlam.ftesterlab.domain.tests.models.TestResult;
import com.dedlam.ftesterlab.domain.tests.models.TestResultRepository;
import com.dedlam.ftesterlab.domain.tests.services.dto.StartTestDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestSubmitDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestWithTestResult;
import com.dedlam.ftesterlab.domain.university.database.StudentsInfoRepository;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.*;

@Service
@RequiredArgsConstructor
public class StudentTestServiceImpl implements StudentTestService {

    private final DeadlineRepository deadlineRepository;
    private final StudentsInfoRepository studentsInfoRepository;
    private final TestResultRepository testResultRepository;
    private final CheckTestService checkTestService;


    @Override
    @Transactional
    public Page<Deadline> incomingDeadlines(Person user, Pageable pageable) {
        var studentInfo = studentsInfoRepository.findStudentInfoByStudent_Id(user.getId())
                .orElseThrow(() -> new BaseException("not a student", NOT_FOUND));
        return deadlineRepository.findByGroup(studentInfo.getGroup(), pageable);
    }

    @Override
    @Transactional
    public TestWithTestResult startTest(StartTestDto startTestDto, Person user) {
        var studentInfo = studentsInfoRepository.findStudentInfoByStudent_Id(user.getId())
                .orElseThrow(() -> new BaseException("not a student", NOT_FOUND));
        var deadline = deadlineRepository.findById(startTestDto.deadlineId())
                .orElseThrow(() -> new BaseException("no such deadline", NOT_FOUND));
        if (!Objects.equals(studentInfo.getGroup(), deadline.getGroup())) {
            throw new BaseException("test not assigned to this group", FORBIDDEN);
        }
        if (!deadline.getTest().getIsOpen()) throw new BaseException("test closed", BAD_REQUEST);
        var initTestResult = new TestResult();
        initTestResult.setDeadline(deadline);
        initTestResult.setStudent(studentInfo);
        initTestResult.setStartedAt(ZonedDateTime.now(ZoneId.systemDefault()));
        var testResult = testResultRepository.save(initTestResult);
        return new TestWithTestResult(deadline.getTest(), testResult);
    }

    @Override
    @Transactional
    public TestResult submitTest(TestSubmitDto submitDto, Person user) {
        var studentInfo = studentsInfoRepository.findStudentInfoByStudent_Id(user.getId())
                .orElseThrow(() -> new BaseException("not a student", NOT_FOUND));
        var testResult = testResultRepository.findById(submitDto.testResultId())
                .orElseThrow(() -> new BaseException("no such test result", NOT_FOUND));
        if (!Objects.equals(studentInfo.getGroup(), testResult.getDeadline().getGroup())) {
            throw new BaseException("test not assigned to this group", FORBIDDEN);
        }
        var test = testResult.getDeadline().getTest();
        if (!test.getIsOpen()) throw new BaseException("test closed", BAD_REQUEST);

        var deadlineTime = testResult.getDeadline().getDeadline();
        var testFinishedAt = ZonedDateTime.now(ZoneId.systemDefault());
        if (deadlineTime.isBefore(testFinishedAt)) throw new BaseException("deadline ended", BAD_REQUEST);

        var result = checkTestService.getResult(test, submitDto);
        var resultInPercents = Math.round(result * 100);
        testResult.setResult(resultInPercents);
        testResult.setFinishedAt(testFinishedAt);
        return testResultRepository.save(testResult);
    }
}
