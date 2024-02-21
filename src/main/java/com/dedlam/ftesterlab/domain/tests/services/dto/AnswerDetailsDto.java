package com.dedlam.ftesterlab.domain.tests.services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerDetailsDto(

        @NotBlank
        String description,

        @NotNull
        Boolean isCorrect
) {
}
