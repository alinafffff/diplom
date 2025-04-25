package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateCreateDTO {
    private Integer id;
    private String login;
    private String email;
    private String roleName;

}
