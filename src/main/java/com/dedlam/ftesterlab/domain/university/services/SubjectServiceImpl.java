package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.people.models.Person;
import com.dedlam.ftesterlab.domain.university.services.dto.SubjectCreateDto;
import com.dedlam.ftesterlab.domain.university.database.SubjectRepository;
import com.dedlam.ftesterlab.domain.university.database.TeachersInfoRepository;
import com.dedlam.ftesterlab.domain.university.models.Subject;
import com.dedlam.ftesterlab.utils.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.BAD_REQUEST;
import static com.dedlam.ftesterlab.utils.exceptions.ExceptionType.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final TeachersInfoRepository teachersInfoRepository;

    @Override
    @Transactional
    public Subject create(SubjectCreateDto createDto, Person owner) {
        var ownerTeacherInfo = teachersInfoRepository.findByTeacher_Id(owner.getId()).orElseThrow(() -> new BaseException(NOT_FOUND));
        if (subjectRepository.existsByName(createDto.name())) throw new BaseException("subject with name already exists", BAD_REQUEST);
        var entity = new Subject(null, createDto.name(), ownerTeacherInfo);
        return subjectRepository.save(entity);
    }

    @Override
    @Transactional
    public Page<Subject> subjects(Person owner, Pageable pageable) {
        return subjectRepository.findByTeacher_Teacher_Id(owner.getId(), pageable);
    }
}
