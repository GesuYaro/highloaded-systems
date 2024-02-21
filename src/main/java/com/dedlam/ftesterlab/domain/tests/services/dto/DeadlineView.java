package com.dedlam.ftesterlab.domain.tests.services.dto;


import java.time.ZonedDateTime;
import java.util.UUID;

public record DeadlineView(
        UUID id,
        UUID testId,
        UUID groupId,
        ZonedDateTime deadline
) {
}
