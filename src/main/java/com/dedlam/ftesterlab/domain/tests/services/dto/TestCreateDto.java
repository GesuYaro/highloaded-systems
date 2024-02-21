package com.dedlam.ftesterlab.domain.tests.services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public record TestCreateDto(

        @NotBlank
        String name,

        @NotNull
        UUID subjectId,

        @NotNull
        Duration duration,

        @NotEmpty
        List<QuestionDetailsDto> questions
) {
}
