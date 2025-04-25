package ru.diplom.diplom.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "author_id", nullable = false)
    private Integer author;

    @Column(name = "my_group_id")
    private Integer myGroup;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "is_student_council_request", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isStudentCouncilRequest = false;

    @Column(name = "is_rejected", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isRejected = false;

}
