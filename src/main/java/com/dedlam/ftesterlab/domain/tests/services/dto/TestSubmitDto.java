package com.dedlam.ftesterlab.domain.tests.services.dto;

import java.util.List;
import java.util.UUID;

public record TestSubmitDto(
        UUID testResultId,
        List<AnswerSubmitDto> answers
) {
}
