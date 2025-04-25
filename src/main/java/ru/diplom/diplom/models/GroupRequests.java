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
@Table(name = "my_group_requests")
public class GroupRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "my_user_id", nullable = false)
    private Integer user;

    @Column(name = "my_group_id", nullable = false)
    private Integer group;
}
