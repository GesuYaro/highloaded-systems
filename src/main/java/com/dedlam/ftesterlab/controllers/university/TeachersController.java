package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.auth.database.UsersRepository;
import com.dedlam.ftesterlab.controllers.BaseController;
import com.dedlam.ftesterlab.domain.people.services.PeopleService;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import com.dedlam.ftesterlab.domain.university.services.SubjectService;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectCreateDto;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectView;
import jakarta.security.auth.message.AuthException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teachers")
public class TeachersController extends BaseController {

    private final SubjectService subjectService;

    public TeachersController(UsersRepository usersRepository, PeopleService peopleService, SubjectService subjectService) {
        super(usersRepository, peopleService);
        this.subjectService = subjectService;
    }

    @PostMapping("/subjects")
    public SubjectView createSubject(@RequestBody SubjectCreateDto createDto) throws AuthException {
        var username = username();
        return toSubjectView(subjectService.create(createDto, username));
    }

    @GetMapping("/subjects")
    public Page<SubjectView> subjects(Pageable pageable) throws AuthException {
        var username = username();
        return subjectService.subjects(username, pageable).map(TeachersController::toSubjectView);
    }

    private static SubjectView toSubjectView(Subject subject) {
        return new SubjectView(subject.getId(), subject.getName());
    }
}
