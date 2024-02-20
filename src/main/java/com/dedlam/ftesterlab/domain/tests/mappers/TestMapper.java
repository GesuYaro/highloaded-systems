package com.dedlam.ftesterlab.domain.tests.mappers;

import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.tests.services.dto.AnswerView;
import com.dedlam.ftesterlab.domain.tests.services.dto.QuestionView;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestView;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectView;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TestMapper {

    public TestView toTestView(Test test) {
        var questions = test.getQuestions().stream()
                .map(question -> {
                    var answers = question.getAnswers().stream()
                            .map(answer -> new AnswerView(
                                    answer.getId(),
                                    answer.getDescription(),
                                    answer.getIsCorrect())
                            )
                            .toList();
                    return new QuestionView(
                            question.getId(),
                            question.getDescription(),
                            question.getQuestionType(),
                            answers
                    );
                })
                .toList();
        return new TestView(
                test.getId(),
                test.getName(),
                toSubjectView(test.getSubject()),
                test.getDuration(),
                test.getIsOpen(),
                questions
        );
    }

    private SubjectView toSubjectView(Subject subject) {
        return new SubjectView(subject.getId(), subject.getName());
    }
}
