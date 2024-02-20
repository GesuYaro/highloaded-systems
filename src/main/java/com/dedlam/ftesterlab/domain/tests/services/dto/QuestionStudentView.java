package com.dedlam.ftesterlab.domain.tests.services.dto;

import com.dedlam.ftesterlab.domain.tests.models.Question;

import java.util.List;
import java.util.UUID;

public record QuestionStudentView(
        UUID id,
        String description,
        Question.QuestionType questionType,
        List<AnswerStudentView> answers
) {
}
