package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.UserAdminDTO;
import ru.diplom.diplom.dto.UserGroupDTO;
import ru.diplom.diplom.dto.UserUpdateCreateDTO;
import ru.diplom.diplom.models.User;
import ru.diplom.diplom.services.ProfileService;
import ru.diplom.diplom.services.UserService;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/all")
    public List<UserAdminDTO> getAllUsers() {
        return userService.getAllUsersAdmin();
    }

    @GetMapping("/byRole")
    public List<UserAdminDTO> getUsersAdminByRole(@RequestParam Integer roleId) {
        return userService.getUsersAdminByRole(roleId);
    }

    @GetMapping("/byUserId")
    public ResponseEntity<User> getUserAdminById(@RequestParam Integer userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Integer userId,
            @RequestBody UserUpdateCreateDTO request) { // Принимаем JSON
        userService.updateUser(userId, request);
        return ResponseEntity.ok("Пользователь успешно обновлен");
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserUpdateCreateDTO request) {
        userService.createUser(request);
        return ResponseEntity.ok("Пользователь успешно создан");
    }

    @PutMapping("/{userId}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Integer userId) {
        boolean isBlocked = userService.blockUser(userId);
        if (isBlocked) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable Integer userId) {
        boolean isUnblocked = userService.unblockUser(userId);
        if (isUnblocked) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserAdminDTO>> searchUsers(@RequestParam String query, @RequestParam String roleName) {
        List<UserAdminDTO> users = userService.searchUsers(query, roleName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/byGroup/{groupId}")
    public ResponseEntity<List<UserGroupDTO>> getUsersByGroup(@PathVariable Integer groupId) {
        List<UserGroupDTO> users = userService.getUsersByGroupId(groupId);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/removeFromGroup/{userId}")
    public ResponseEntity<Void> removeFromGroup(@PathVariable Integer userId) {
        userService.removeStudentFromGroup(userId);
        return ResponseEntity.ok().build();
    }


}
