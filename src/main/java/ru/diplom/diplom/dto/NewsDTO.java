package ru.diplom.diplom.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDTO {
    private Integer id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Integer author;
    private String authorRole;
    private String title;
    private String description;
    private String photoUrl;
}
