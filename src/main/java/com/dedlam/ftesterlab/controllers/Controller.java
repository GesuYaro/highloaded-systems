package com.dedlam.ftesterlab.controllers;

import com.dedlam.ftesterlab.domain.people.StudentInfo;
import com.dedlam.ftesterlab.domain.people.StudentsInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class Controller {
  private final StudentsInfoRepository repository;

  @Autowired
  public Controller(StudentsInfoRepository repository) {
    this.repository = repository;
  }

  @GetMapping("sir")
  String sir() {
    return "SIR! YES, SIR!";
  }

  @GetMapping("/test/1")
  String test1() {
    var student = new StudentInfo(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    repository.save(student);
    return student.toString();
  }

  @GetMapping("/test/2")
  String test2() {
    var students = repository.findAll();
    return students.toString();
  }
}