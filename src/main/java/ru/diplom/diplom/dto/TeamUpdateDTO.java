package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamUpdateDTO {
    private Integer id;
    private Integer place;
    private String diploma;
    private Boolean isConfirmed;
}
