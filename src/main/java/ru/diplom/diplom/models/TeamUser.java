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
@Table(name = "team_my_user")
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "my_user_id", nullable = false)
    private Integer user;

    @Column(name = "team_id", nullable = false)
    private Integer team;

}
