package ru.diplom.diplom.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupAllArchivedDTO {
    private Integer id;
    private String directionAbbreviation;
    private String formName;
    private String levelName;
    private Integer profileNumber;
    private Integer endYear;
    private Integer number;
    private boolean archived;
}
