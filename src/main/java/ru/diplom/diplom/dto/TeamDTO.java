package ru.diplom.diplom.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamDTO {
    private Integer teamId;
    private String teamName;
    private List<TeamMateDTO> teamMates;

}
