package com.dedlam.ftesterlab.domain.university.services;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.university.database.TeachersInfoRepository;
import com.dedlam.ftesterlab.domain.university.models.TeacherInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachersServiceImpl implements TeachersService {
  private final TeachersInfoRepository repository;

  public TeachersServiceImpl(TeachersInfoRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean createAndInitTeachersInfo(Person teacher) {
    var teacherInfo = new TeacherInfo(null, teacher, List.of());

    try {
      repository.save(teacherInfo);
      return true;
    } catch (DataAccessException e) {
      return false;
    }
  }
}
