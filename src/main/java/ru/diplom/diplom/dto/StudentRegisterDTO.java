package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisterDTO {
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phone;
    private String login;
    private String password;
    private Integer groupId; // может быть null
}
