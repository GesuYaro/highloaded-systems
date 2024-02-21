package com.dedlam.ftesterlab.domain.tests.services;

import com.dedlam.ftesterlab.domain.tests.models.Test;
import com.dedlam.ftesterlab.domain.tests.services.dto.TestSubmitDto;

public interface CheckTestService {
    Double getResult(Test test, TestSubmitDto submitDto);
}
