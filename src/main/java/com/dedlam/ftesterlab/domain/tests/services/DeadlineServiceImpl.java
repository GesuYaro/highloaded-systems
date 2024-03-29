package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.tests.database.DeadlineRepository;
import com.dedlam.ftesterlab.domain.tests.database.TestRepository;
import com.dedlam.ftesterlab.domain.tests.models.Deadline;
import com.dedlam.ftesterlab.domain.tests.services.dto.DeadlineCreateDto;
import com.dedlam.ftesterlab.domain.university.database.GroupsRepository;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.FORBIDDEN;
import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DeadlineServiceImpl implements DeadlineService {
    private final PeopleService peopleService;
    private final DeadlineRepository deadlineRepository;
    private final TestRepository testRepository;
    private final GroupsRepository groupsRepository;

    @Override
    public Page<Deadline> deadlines(Person user, Pageable pageable) {
        return deadlineRepository.findByTest_Teacher_TeacherId(user.getId(), pageable);
    }

    @Override
    @Transactional
    public Deadline createDeadline(DeadlineCreateDto createDto, Person user) {
        var test = testRepository.findById(createDto.testId())
                .orElseThrow(() -> new BaseException("no such test", NOT_FOUND));
        var testOwner = peopleService.person(test.getTeacher().getTeacherId());
        if (testOwner == null) throw new BaseException("Can't find person", NOT_FOUND);
        if (!testOwner.getId().equals(user.getId())) throw new BaseException("test belongs to another user", FORBIDDEN);
        var group = groupsRepository.findById(createDto.groupId())
                .orElseThrow(() -> new BaseException("no such group", NOT_FOUND));
        var newDeadline = new Deadline();
        newDeadline.setTest(test);
        newDeadline.setGroup(group);
        newDeadline.setDeadline(createDto.deadline());
        return deadlineRepository.save(newDeadline);
    }
}
