package com.dedlam.ftesterlab.domain.people.services;

import java.util.UUID;

public record CreatePersonRequest(
  UUID userId,
  PersonDto personInfo
) {
}
