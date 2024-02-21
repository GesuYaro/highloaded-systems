package com.dedlam.ftesterlab.domain.tests.services.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record TestSubmitDto(

        @NotNull
        UUID testResultId,

        @NotEmpty
        List<AnswerSubmitDto> answers
) {
}
