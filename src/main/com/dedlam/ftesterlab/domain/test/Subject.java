package dedlam.ftesterlab.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    private UUID id;

    
}
