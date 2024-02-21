package com.dedlam.ftesterlab.domain.tests.services.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TestSearchDto(

        @NotNull
        UUID subjectId
) {
}
