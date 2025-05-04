package ru.diplom.diplom.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUpdateDTO {
    Integer id;
    private String leaderFullName;      // ФИО старосты
    private String organizerFullName;   // ФИО профорга
    private String description;
}
