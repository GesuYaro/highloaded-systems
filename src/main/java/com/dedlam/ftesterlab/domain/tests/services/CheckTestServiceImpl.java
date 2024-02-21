package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.tests.models.Answer;
import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.tests.services.dto.AnswerSubmitDto;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestSubmitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dedlam.ftesterlab.domain.tests.models.Question.QuestionType.OPEN;

@Service
@RequiredArgsConstructor
public class CheckTestServiceImpl implements CheckTestService {


    @Override
    public Double getResult(Test test, TestSubmitDto submitDto) {
        var answerMap = submitDto.answers().stream()
                        .collect(Collectors.toMap(
                                AnswerSubmitDto::questionId, answer -> answer
                        ));
        return test.getQuestions().stream()
                .mapToDouble(question -> {
                    if (question.getQuestionType() == OPEN) {
                        var answ = answerMap.get(question.getId());
                        if (answ == null) return 0;
                        return calculateOpenQuestion(question.getAnswers(), answ.openAnswer());
                    } else {
                        var answ = answerMap.get(question.getId());
                        if (answ == null) return 0;
                        return calculateSeveralAnswersQuestion(question.getAnswers(), answ.answerIds());
                    }
                })
                .average().orElse(0);
    }

    private Double calculateOpenQuestion(List<Answer> answers, String userAnswer) {
        var correspondingCorrectAnswer = answers.stream()
                .filter(answer -> Objects.equals(answer.getDescription(), userAnswer))
                .findAny();
        if (correspondingCorrectAnswer.isPresent() && correspondingCorrectAnswer.get().getIsCorrect()) return 1d;
        else return 0d;
    }

    private Double calculateSeveralAnswersQuestion(List<Answer> answers, List<UUID> userAnswers) {
        var answersSet = new HashSet<>(userAnswers);
        return answers.stream()
                .mapToDouble(answer -> {
                    if (answer.getIsCorrect()) {
                        if (answersSet.contains(answer.getId())) {
                            return 1d;
                        } else {
                            return 0d;
                        }
                    } else {
                        if (answersSet.contains(answer.getId())) {
                            return 0d;
                        } else {
                            return 1d;
                        }
                    }
                })
                .average().orElse(0);
    }
}
