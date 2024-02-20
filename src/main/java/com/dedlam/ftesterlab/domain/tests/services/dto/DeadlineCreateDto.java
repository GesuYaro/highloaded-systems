package com.dedlam.ftesterlab.domain.tests.services.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record DeadlineCreateDto(
        UUID testId,
        UUID groupId,
        ZonedDateTime deadline
) {
}
