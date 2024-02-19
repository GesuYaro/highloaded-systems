package com.dedlam.ftesterlab.domain.tests.services.dto;

import com.dedlam.ftesterlab.domain.tests.models.Question;

import java.util.List;

public record QuestionDetailsDto(
        String description,
        Question.QuestionType questionType,
        List<AnswerDetailsDto> answers
) {
}
