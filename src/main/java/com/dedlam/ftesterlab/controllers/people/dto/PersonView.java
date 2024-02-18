package com.dedlam.ftesterlab.controllers.people.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record PersonView(
  String id,
  String name,
  String middleName,
  String lastName,
  @JsonFormat(pattern = "dd.MM.yyyy") LocalDate birthday
) {
}