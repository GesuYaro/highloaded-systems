package com.dedlam.ftesterlab.domain.tests.services.dto;

import java.util.UUID;

public record AnswerView(
        UUID id,
        String description,
        Boolean isCorrect
) {
}
