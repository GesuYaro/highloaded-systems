package com.dedlam.ftesterlab.domain.tests.services.dto;

import java.util.List;
import java.util.UUID;

public record AnswerSubmitDto(
        UUID questionId,
        List<UUID> answerIds,
        String openAnswer
) {
}
