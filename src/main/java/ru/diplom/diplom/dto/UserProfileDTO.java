package ru.diplom.diplom.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
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
    public String getPhone() {
        return phone == null ? "Не указан" : phone;
    }
    public String getPatronymic() {
        return patronymic == null ? "" : patronymic;
    }
}
