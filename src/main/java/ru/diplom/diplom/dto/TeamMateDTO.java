package ru.diplom.diplom.dto;

import lombok.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMateDTO {
    private Integer id;
    private String surname;
    private String name;
    private String patronymic;
    private String groupName;
}