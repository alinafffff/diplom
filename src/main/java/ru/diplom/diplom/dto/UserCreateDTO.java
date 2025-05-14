package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;
    private String login;
    private String email;
    private String roleName;

}
