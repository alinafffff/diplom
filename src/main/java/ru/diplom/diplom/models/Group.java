package ru.diplom.diplom.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "my_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "profile_id")
    private Integer profile; // Может быть NULL, если у группы нет профиля

    @Column(name = "direction_id", nullable = false)
    private Integer direction; // Всегда NOT NULL

    @Column(name = "form_id")
    private Integer form;

    @Column(name = "my_level_id", nullable = false)
    private Integer myLevel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "leader_id")
    private Integer leader;

    @Column(name = "organizer_id")
    private Integer organizer;

    @Column(name = "curator_id")
    private Integer curator;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}
