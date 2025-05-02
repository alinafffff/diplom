package ru.diplom.diplom.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.SQLType;
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
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "notification_type", nullable = false)
    private NotificationType type;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT now()", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "my_group_id")
    private Integer myGroup;

    @Column(name = "reciever_id")
    private Integer receiver;

    @Column(name = "is_read", nullable = false, columnDefinition = "boolean default false")
    private boolean isRead = false;
}

