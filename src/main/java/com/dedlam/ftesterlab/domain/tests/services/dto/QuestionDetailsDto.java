package com.dedlam.ftesterlab.domain.tests.services.dto;

import com.dedlam.ftesterlab.domain.tests.models.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionDetailsDto(

        @NotBlank
        String description,

        @NotNull
        Question.QuestionType questionType,

        @NotEmpty
        List<AnswerDetailsDto> answers
) {
}
