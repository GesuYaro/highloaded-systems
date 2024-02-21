package com.dedlam.ftesterlab.domain.tests.services.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TestChangeStateDto(

        @NotNull
        UUID testId,

        @NotNull
        Boolean isOpen
) {
}
