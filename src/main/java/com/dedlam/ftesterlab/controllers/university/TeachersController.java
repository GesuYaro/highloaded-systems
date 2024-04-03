package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.tests.mappers.DeadlineMapper;
import com.dedlam.ftesterlab.domain.tests.mappers.TestMapper;
import com.dedlam.ftesterlab.domain.tests.services.DeadlineService;
import com.dedlam.ftesterlab.domain.tests.services.TestService;
import com.dedlam.ftesterlab.domain.tests.services.dto.*;
import com.dedlam.ftesterlab.domain.university.models.Group;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import com.dedlam.ftesterlab.domain.university.services.GroupsService;
import com.dedlam.ftesterlab.domain.university.services.SubjectService;
import com.dedlam.ftesterlab.domain.university.services.dto.GroupView;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectCreateDto;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectView;
import com.dedlam.ftesterlab.domain.users.UserService;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
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
    private final GroupsService groupsService;

    public TeachersController(
            PeopleService peopleService,
            SubjectService subjectService,
            TestMapper testMapper,
            TestService testService,
            DeadlineService deadlineService,
            DeadlineMapper deadlineMapper,
            GroupsService groupsService,
            UserService userService
    ) {
        super(userService, peopleService);
        this.subjectService = subjectService;
        this.testMapper = testMapper;
        this.testService = testService;
        this.deadlineService = deadlineService;
        this.deadlineMapper = deadlineMapper;
        this.groupsService = groupsService;
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

    @PostMapping("/tests/state")
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

    @GetMapping("/groups")
    public Window<GroupView> groups(OffsetScrollPosition offsetScrollPosition) {
        return groupsService.groups(offsetScrollPosition).map(TeachersController::toGroupView);
    }

    private static SubjectView toSubjectView(Subject subject) {
        return new SubjectView(subject.getId(), subject.getName());
    }

    private static GroupView toGroupView(Group group) {
        return new GroupView(group.getId(), group.getName(), group.getGrade());
    }
}
