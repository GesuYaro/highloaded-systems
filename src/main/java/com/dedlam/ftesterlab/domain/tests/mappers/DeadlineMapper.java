package com.dedlam.ftesterlab.domain.tests.mappers;

import com.dedlam.ftesterlab.domain.tests.models.Deadline;
import com.dedlam.ftesterlab.domain.tests.services.dto.DeadlineView;
import org.springframework.stereotype.Component;

@Component
public class DeadlineMapper {

    public DeadlineView toDeadlineView(Deadline deadline) {
        return new DeadlineView(
                deadline.getId(),
                deadline.getTest().getId(),
                deadline.getGroup().getId(),
                deadline.getDeadline()
        );
    }
}
