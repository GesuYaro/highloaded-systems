package com.dedlam.ftesterlab.domain.tests.services.dto;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public record TestCreateDto(
        String name,
        UUID subjectId,
        Duration duration,
        List<QuestionDetailsDto> questions
) {
}
