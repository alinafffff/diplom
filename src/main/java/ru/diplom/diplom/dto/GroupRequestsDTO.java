package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class GroupRequestsDTO {
    private Integer id;
    private String fullName;

    private String groupName;
    private Integer groupId;
    public String getFullName() {
        return fullName == null || fullName.equals("null null null") ? "" : fullName;
    }
}
