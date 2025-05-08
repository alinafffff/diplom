package ru.diplom.diplom.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEventShortDTO{
    Integer id;
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    public String getEndDate() {
        return endDate == null ? "" : endDate.toString();
    }
}
