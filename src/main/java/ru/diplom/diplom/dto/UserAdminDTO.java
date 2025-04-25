package ru.diplom.diplom.dto;
import jakarta.persistence.*;
import lombok.*;
import ru.diplom.diplom.models.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAdminDTO {
    private Integer id;
    private String login;
    private String email;
    private String fullName;
    private String roleName;
    private String status;
    public String getFullName() {
        return fullName == null || fullName.equals("null null null") ? "" : fullName;
    }
}