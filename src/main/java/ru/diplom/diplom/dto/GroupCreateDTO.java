package ru.diplom.diplom.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupCreateDTO {
    private Integer id;
    private String directionAbbreviation;
    private String formName;
    private String levelName;
    private String profileName;
    private LocalDate startDate;
    private Integer duration;
    private Integer number;
}
