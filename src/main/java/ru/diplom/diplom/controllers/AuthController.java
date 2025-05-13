package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.LoginRequestDTO;

import java.util.Map;

@RestController
@RequestMapping("/inpit/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            String sql = "SELECT u.id, r.name AS role " +
                    "FROM my_user u " +
                    "JOIN my_role r ON u.my_role_id = r.id " +
                    "WHERE u.login = ? AND u.password = ?";

            Map<String, Object> user = jdbcTemplate.queryForMap(sql, request.getLogin(), request.getPassword());
            String role = (String) user.get("role");
            System.out.println("Роль пользователя: '" + role + "'");

            if (role.equalsIgnoreCase("Студент") || role.equalsIgnoreCase("Студсовет")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вход запрещён для этой роли");
            }

            return ResponseEntity.ok(Map.of(
                    "id", user.get("id"),
                    "role", role
            ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }
}
