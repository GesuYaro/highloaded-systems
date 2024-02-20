package com.dedlam.ftesterlab.domain.tests.models;

import com.dedlam.ftesterlab.domain.university.models.Group;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "deadlines")
@NoArgsConstructor
@Getter
@Setter
public class Deadline {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "deadline")
    private ZonedDateTime deadline;
}
