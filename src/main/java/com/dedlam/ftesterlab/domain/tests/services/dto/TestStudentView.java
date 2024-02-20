package com.dedlam.ftesterlab.domain.tests.services.dto;

import com.dedlam.ftesterlab.domain.university.services.dto.SubjectView;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public record TestStudentView(
        UUID id,
        String name,
        SubjectView subject,
        Duration duration,
        Boolean isOpen,
        List<QuestionStudentView> questions
) {
}
