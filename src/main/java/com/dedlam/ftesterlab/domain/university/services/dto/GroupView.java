package com.dedlam.ftesterlab.domain.university.services.dto;

import java.util.UUID;

public record GroupView(
        UUID id,
        String name,
        Integer grade
) {
}
