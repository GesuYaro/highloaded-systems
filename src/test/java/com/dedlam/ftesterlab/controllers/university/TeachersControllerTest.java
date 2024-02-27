package com.dedlam.ftesterlab.controllers.university;

import com.dedlam.ftesterlab.FTesterLabApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TeachersControllerTest extends FTesterLabApplication {

    @Test
    void groups() {
        System.out.println(UUID.randomUUID());
    }
}