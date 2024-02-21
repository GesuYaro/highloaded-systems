package com.dedlam.ftesterlab.domain.tests.services.dto;

import com.dedlam.ftesterlab.domain.university.services.dto.SubjectView;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public record TestView(
        UUID id,
        String name,
        SubjectView subject,
        Duration duration,
        Boolean isOpen,
        List<QuestionView> questions
) {
}
