package ru.diplom.diplom.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGroupPointsDTO {
    private Integer id;
    private String fullName;
    private String groupName;
    private Integer points;
    private Integer groupId;
}
