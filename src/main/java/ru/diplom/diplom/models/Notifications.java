package ru.diplom.diplom.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "type", nullable = false)
    private String type;//enum?

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT now()", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "my_group_id")
    private Integer myGroup;

    @Column(name = "reciever_id")
    private Integer receiver;
}

//public enum NotificationType {
//    СИСТЕМА, НОВОСТЬ;
//}
