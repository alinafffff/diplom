package ru.diplom.diplom.dto;

import jakarta.persistence.Column;
import lombok.*;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.EventType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveEventDTO {
    private Integer id;

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventType type;
    private Integer createdBy;
    private Integer points;
    private Integer points1st;
    private Integer points2nd;
    private Integer points3rd;
    private Integer pointsParticipation;
    private String photoUrl;
    private Integer maxParticipants;
    private Integer maxTeamSize;
    private Boolean isStudentCouncilRequest = false;
    private Boolean isRejected;

    public Event toEntity() {
        return Event.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .type(this.type)
                .createdBy(this.createdBy)
                .points(this.points)
                .points1st(this.points1st)
                .points2nd(this.points2nd)
                .points3rd(this.points3rd)
                .pointsParticipation(this.pointsParticipation)
                .photoUrl(this.photoUrl)
                .maxParticipants(this.maxParticipants)
                .maxTeamSize(this.maxTeamSize)
                .isStudentCouncilRequest(this.isStudentCouncilRequest)
                .isRejected(this.isRejected)
                .build();
    }

}
