package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentProfileDTO {
    private Integer id;
    private String surname;
    private String name;
    private String patronymic;
    private String groupName;
    private String phone;
    private String email;
    private Integer points;
    private Integer groupId;
    private String photoUrl;
    private Integer role;

    public StudentProfileDTO(UserProfileDTO dto, int role){
        this.id= dto.getId();
        this.surname = dto.getSurname();
        this.name = dto.getName();
        this.patronymic = dto.getPatronymic();
        this.groupName= dto.getGroupName();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.points = dto.getPoints();
        this.groupId = dto.getGroupId();
        this.photoUrl=dto.getPhotoUrl();
        this.role = role;
    }
}

