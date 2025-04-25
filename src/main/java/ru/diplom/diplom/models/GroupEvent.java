package ru.diplom.diplom.models;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "my_group_my_event")
public class GroupEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "my_group_id", nullable = false)
    private Integer group;

    @Column(name = "my_event_id", nullable = false)
    private Integer event;

}
