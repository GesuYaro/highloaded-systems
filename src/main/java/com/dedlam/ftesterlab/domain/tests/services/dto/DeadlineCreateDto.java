package com.dedlam.ftesterlab.domain.tests.services.dto;

import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public record DeadlineCreateDto(

        @NotNull
        UUID testId,

        @NotNull
        UUID groupId,

        @NotNull
        ZonedDateTime deadline
) {
}
