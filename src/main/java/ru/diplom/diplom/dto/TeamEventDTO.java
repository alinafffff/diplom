package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamEventDTO {
    private Integer id;
    private String teamName;
    private String eventName;
    private Integer place;
    private String diploma;
    private Integer eventId;
    private Boolean isConfirmed;

    public Integer getPlace() {
        return place == null ? null : Integer.valueOf(place);  // place — строка
    }


    public String getDiploma() {
        return diploma == null ? "" : diploma;
    }

}

