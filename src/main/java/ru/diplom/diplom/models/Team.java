package ru.diplom.diplom.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "my_event_id", nullable = false)
    private Integer myEvent;

    @Column(name = "place")
    private Integer place;

    @Column(name = "diploma")
    private String diploma;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @PrePersist
    @PreUpdate
    private void validatePlace() {
        if (place != null && (place < 1 || place > 3)) {
            throw new IllegalArgumentException("Place must be between 1 and 3");
        }
    }
}
