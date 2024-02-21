package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.tests.database.TestRepository;
import com.dedlam.ftesterlab.domain.tests.models.Answer;
import com.dedlam.ftesterlab.domain.tests.models.Question;
import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.tests.services.dto.*;
import com.dedlam.ftesterlab.domain.university.database.SubjectRepository;
import com.dedlam.ftesterlab.domain.university.database.TeachersInfoRepository;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.FORBIDDEN;
import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final SubjectRepository subjectRepository;
    private final TeachersInfoRepository teachersInfoRepository;

    @Override
    public Page<Test> tests(TestSearchDto searchDto, Pageable pageable) {
        return testRepository.findBySubject_Id(searchDto.subjectId(), pageable);
    }

    @Override
    @Transactional
    public Test createTest(TestCreateDto createDto, Person owner) {
        return testRepository.save(toEntity(createDto, owner.getId()));
    }

    @Override
    @Transactional
    public Test changeTestOpenState(TestChangeStateDto changeStateDto, Person owner) {
        var test = testRepository.findById(changeStateDto.testId())
                .orElseThrow(() -> new BaseException("test not found", NOT_FOUND));
        var teacher = teachersInfoRepository.findByTeacher_Id(owner.getId())
                .orElseThrow(() -> new BaseException("teacher not found", NOT_FOUND));
        var teacherId = teacher.getId();
        var testOwnerId = test.getTeacher().getId();
        if (!testOwnerId.equals(teacherId)) throw new BaseException("test belongs to another user", FORBIDDEN);
        if (test.getIsOpen() == changeStateDto.isOpen()) return null;
        test.setIsOpen(changeStateDto.isOpen());
        return testRepository.save(test);
    }

    private Test toEntity(TestCreateDto createDto, UUID ownerId) {
        var subject = subjectRepository.findByIdAndTeacher_Teacher_Id(createDto.subjectId(), ownerId)
                .orElseThrow(() -> new BaseException("No such subject for this user", NOT_FOUND));
        var teacher = teachersInfoRepository.findByTeacher_Id(ownerId)
                .orElseThrow(() -> new BaseException("teacher not found", NOT_FOUND));
        var testEntity = new Test();
        var questions = toQuestionEntityList(createDto.questions(), testEntity);
        testEntity.setName(createDto.name());
        testEntity.setSubject(subject);
        testEntity.setDuration(createDto.duration());
        testEntity.setQuestions(questions);
        testEntity.setIsOpen(true);
        testEntity.setTeacher(teacher);
        return testEntity;
    }

    private List<Question> toQuestionEntityList(List<QuestionDetailsDto> detailsDtos, Test test) {
        return detailsDtos.stream()
                .map(dto -> {
                    var questionEntity = new Question();
                    var answers = toAnswerEntityList(dto.answers(), questionEntity);
                    questionEntity.setTest(test);
                    questionEntity.setQuestionType(dto.questionType());
                    questionEntity.setDescription(dto.description());
                    questionEntity.setAnswers(answers);
                    return questionEntity;
                })
                .toList();
    }

    private List<Answer> toAnswerEntityList(List<AnswerDetailsDto> detailsDtos, Question question) {
        return detailsDtos.stream()
                .map(dto -> new Answer(question, dto.description(), dto.isCorrect()))
                .toList();
    }
}
