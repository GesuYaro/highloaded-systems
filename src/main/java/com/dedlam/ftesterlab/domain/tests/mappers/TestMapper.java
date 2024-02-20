package com.dedlam.ftesterlab.domain.tests.mappers;

import com.dedlam.ftesterlab.domain.tests.models.Answer;
import com.dedlam.ftesterlab.domain.tests.models.Question;
import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.tests.models.TestResult;
import com.dedlam.ftesterlab.domain.tests.services.dto.*;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectView;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dedlam.ftesterlab.domain.tests.models.Question.QuestionType.OPEN;

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

    public TestWithTestResultView toTestWithTestResultView(TestWithTestResult testWithTestResult) {
        return new TestWithTestResultView(
                toTestStudentView(testWithTestResult.test()),
                toTestResultView(testWithTestResult.testResult())
        );
    }

    public TestResultView toTestResultView(TestResult testResult) {
        return new TestResultView(
                testResult.getId(),
                testResult.getDeadline().getDeadline(),
                testResult.getResult(),
                testResult.getStartedAt(),
                testResult.getFinishedAt()
        );
    }

    private TestStudentView toTestStudentView(Test test) {
        return new TestStudentView(
                test.getId(),
                test.getName(),
                toSubjectView(test.getSubject()),
                test.getDuration(),
                test.getIsOpen(),
                test.getQuestions().stream().map(this::toQuestionStudentView).toList()
        );
    }

    private QuestionStudentView toQuestionStudentView(Question question) {
        var answers = question.getQuestionType() == OPEN
                ? null
                : question.getAnswers().stream().map(this::toAnswerStudentView).toList();
        return new QuestionStudentView(
                question.getId(),
                question.getDescription(),
                question.getQuestionType(),
                answers
        );
    }

    private AnswerStudentView toAnswerStudentView(Answer answer) {
        return new AnswerStudentView(
                answer.getId(),
                answer.getDescription()
        );
    }

    private SubjectView toSubjectView(Subject subject) {
        return new SubjectView(subject.getId(), subject.getName());
    }
}
