package ru.diplom.diplom.dto;

import lombok.*;
import ru.diplom.diplom.models.EventType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventVolunteeringDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventType type;
    private String authorFullName;
    private Integer points;
    private String photoUrl;
    private Integer maxParticipants;
    private Boolean isStudentCouncilRequest = false;
    private Boolean isRejected;

}
