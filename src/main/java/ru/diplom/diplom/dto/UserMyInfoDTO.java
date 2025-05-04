package ru.diplom.diplom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMyInfoDTO {
    private Integer id;
    private String email;
    private String surname;
    private String name;
    private String patronymic;
    private String photoUrl;
}
