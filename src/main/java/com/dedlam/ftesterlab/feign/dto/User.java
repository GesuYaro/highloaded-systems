package com.dedlam.ftesterlab.feign.dto;

import java.util.UUID;

public record User(
        UUID id,
        String username
) {
}
