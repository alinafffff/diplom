package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupAllDTO {
    private Integer id;
    private String directionAbbreviation;
    private String formName;
    private String levelName;
    private Integer profileNumber;
    private Integer course;
    private Integer number;
    private boolean archived;
}
