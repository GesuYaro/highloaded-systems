package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.domain.university.Subject;
import com.dedlam.ftesterlab.domain.tests.services.SubjectService;
import com.dedlam.ftesterlab.domain.tests.services.dto.SubjectCreateDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.SubjectView;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/teachers")
@RequiredArgsConstructor
public class TeachersController {

    private final SubjectService subjectService;

    @PostMapping("/subjects")
    public SubjectView createSubject(@RequestBody SubjectCreateDto createDto) throws AuthException {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return toSubjectView(subjectService.create(createDto, username));
    }

    @GetMapping("/subjects")
    public Page<SubjectView> subjects(Pageable pageable) throws AuthException {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return subjectService.subjects(username, pageable).map(TeachersController::toSubjectView);
    }



    private static SubjectView toSubjectView(Subject subject) {
        return new SubjectView(subject.getId(), subject.getName());
    }
}
