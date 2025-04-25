package ru.diplom.diplom.models;

import jakarta.persistence.*;
import lombok.*;
import java.security.SecureRandom;
import java.util.Base64;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "my_user")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String name;

    @Column(name = "last_name")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password = generateRandomPassword(12);;

    @Column(name = "phone")
    private String phone;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "points", columnDefinition = "INTEGER DEFAULT 0")
    private Integer points = 0;

    @Column(name = "is_blocked", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isBlocked = false;

    @Column(name = "my_role_id", nullable = false)
    private Integer role;

    @Column(name = "my_group_id")
    private Integer group;

    private String generateRandomPassword(int l) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[l];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, l);
    }
}
