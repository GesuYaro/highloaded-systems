package com.dedlam.ftesterlab.domain.tests.models;

import com.dedlam.ftesterlab.domain.university.models.StudentInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_results")
@NoArgsConstructor
@Getter
@Setter
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentInfo student;

    @ManyToOne
    @JoinColumn(name = "deadlineId")
    private Deadline deadline;

    @Column(name = "result")
    private Long result;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "finished_at")
    private ZonedDateTime finishedAt;

}
