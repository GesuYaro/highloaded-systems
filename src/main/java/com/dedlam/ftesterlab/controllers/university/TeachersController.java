package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.tests.mappers.DeadlineMapper;
import com.dedlam.ftesterlab.domain.tests.services.DeadlineService;
import com.dedlam.ftesterlab.domain.tests.services.TestService;
import com.dedlam.ftesterlab.domain.tests.services.dto.*;
import com.dedlam.ftesterlab.domain.tests.mappers.TestMapper;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import com.dedlam.ftesterlab.domain.university.services.SubjectService;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectCreateDto;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teachers")
public class TeachersController extends BaseController {

    private final SubjectService subjectService;
    private final TestMapper testMapper;
    private final TestService testService;
    private final DeadlineService deadlineService;
    private final DeadlineMapper deadlineMapper;

    public TeachersController(
            UsersRepository usersRepository,
            PeopleService peopleService,
            SubjectService subjectService,
            TestMapper testMapper,
            TestService testService,
            DeadlineService deadlineService,
            DeadlineMapper deadlineMapper
    ) {
        super(usersRepository, peopleService);
        this.subjectService = subjectService;
        this.testMapper = testMapper;
        this.testService = testService;
        this.deadlineService = deadlineService;
        this.deadlineMapper = deadlineMapper;
    }

    @PostMapping("/subjects")
    public SubjectView createSubject(@RequestBody @Validated SubjectCreateDto createDto) {
        var user = person();
        return toSubjectView(subjectService.create(createDto, user));
    }

    @GetMapping("/subjects")
    public Page<SubjectView> subjects(Pageable pageable) {
        var user = person();
        return subjectService.subjects(user, pageable).map(TeachersController::toSubjectView);
    }

    @PostMapping("/tests")
    public TestView createTest(@RequestBody @Validated TestCreateDto testCreateDto) {
        var user = person();
        return testMapper.toTestView(testService.createTest(testCreateDto, user));
    }

    @GetMapping("/tests")
    public Page<TestView> tests(@Validated TestSearchDto testSearchDto, Pageable pageable) {
        return testService.tests(testSearchDto, pageable).map(testMapper::toTestView);
    }

    @PatchMapping("/tests")
    public TestView changeTestState(@RequestBody @Validated TestChangeStateDto changeStateDto) {
        var user = person();
        return testMapper.toTestView(testService.changeTestOpenState(changeStateDto, user));
    }

    @GetMapping("/deadlines")
    public Page<DeadlineView> deadlines(Pageable pageable) {
        var user = person();
        return deadlineService.deadlines(user, pageable).map(deadlineMapper::toDeadlineView);
    }

    @PostMapping("/deadlines")
    public DeadlineView createDeadline(@RequestBody @Validated DeadlineCreateDto createDto) {
        var user = person();
        return deadlineMapper.toDeadlineView(deadlineService.createDeadline(createDto, user));
    }

    private static SubjectView toSubjectView(Subject subject) {
        return new SubjectView(subject.getId(), subject.getName());
    }
}
