package com.dedlam.ftesterlab.controllers;

import com.dedlam.ftesterlab.domain.people.database.Person;
import com.dedlam.ftesterlab.domain.people.services.PeopleServiceImpl;
import com.dedlam.ftesterlab.domain.university.StudentInfo;
import com.dedlam.ftesterlab.domain.university.StudentsInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class Controller {
  private final PeopleServiceImpl service;
  private final StudentsInfoRepository repository;

  @Autowired
  public Controller(PeopleServiceImpl service, StudentsInfoRepository repository) {
    this.service = service;
    this.repository = repository;
  }

  @GetMapping("sir")
  String sir() {
    return "SIR! YES, SIR!";
  }

  @GetMapping("/test/1")
  String test1() {
    var student = new Person(UUID.randomUUID(), "A", "B", "C", LocalDate.now());
    var info = new StudentInfo(UUID.randomUUID(), student);
    repository.save(info);
    return student.toString();
  }

  @GetMapping("/test/2")
  String test2() {
    var students = service.people();
    return students.toString();
  }
}