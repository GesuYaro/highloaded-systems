package com.dedlam.ftesterlab.domain.tests.services.dto;


import java.time.ZonedDateTime;
import java.util.UUID;

public record TestResultView(
        UUID id,
        ZonedDateTime deadline,
        Long result,
        ZonedDateTime startedAt,
        ZonedDateTime finishedAt
) {
}
