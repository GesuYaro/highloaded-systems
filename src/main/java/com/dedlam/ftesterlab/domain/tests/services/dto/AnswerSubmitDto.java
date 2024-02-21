package com.dedlam.ftesterlab.domain.tests.services.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record AnswerSubmitDto(

        @NotNull
        UUID questionId,
        List<UUID> answerIds,
        String openAnswer
) {
}
