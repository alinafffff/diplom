package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDTO {
    private Integer id;
    private String name;
    private Integer number;
    private String directionAbbreviation;

}
