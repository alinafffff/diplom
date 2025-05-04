package ru.diplom.diplom.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "my_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EventType type;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "points")
    private Integer points;

    @Column(name = "points_1st")
    private Integer points1st;

    @Column(name = "points_2nd")
    private Integer points2nd;

    @Column(name = "points_3rd")
    private Integer points3rd;

    @Column(name = "points_participation")
    private Integer pointsParticipation;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "max_team_size")
    private Integer maxTeamSize;

    @Column(name = "is_student_council_request", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isStudentCouncilRequest = false;

    @Column(name = "is_rejected")
    private Boolean isRejected;
}


