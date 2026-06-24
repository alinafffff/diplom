package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.User;
import ru.diplom.diplom.repositories.UserRepository;
import ru.diplom.diplom.services.ProfileService;
import ru.diplom.diplom.services.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<UserAdminDTO> getAllUsers() {
        return userService.getAllUsersAdmin();
    }

    @GetMapping("/allSudentsAndPoints")
    public List<UserGroupPointsDTO> getAllStudentsPoints() {
        return userService.getAllUsersPoints();
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
            @RequestBody UserUpdateCreateDTO request) {
        userService.updateUser(userId, request);
        return ResponseEntity.ok("Пользователь успешно обновлен");
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDTO request) {
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

    @GetMapping("/searchStudents")
    public ResponseEntity<List<UserGroupDTO>> searchUsers(
            @RequestParam("query") String query,
            @RequestParam("groupId") Integer groupId
    ) {
        List<UserGroupDTO> users = userService.searchUsersByFullName(query, groupId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/searchEventStudents")
    public ResponseEntity<List<UserGroupDTO>> searchEventUsers(
            @RequestParam("query") String query,
            @RequestParam("eventId") Integer eventId
    ) {
        List<UserGroupDTO> users = userService.searchUsersByFullNameAndEventId(query,eventId);
        return ResponseEntity.ok(users);
    }


    @GetMapping("/searchStudentsPoints")
    public ResponseEntity<List<UserGroupPointsDTO>> searchStudents(
            @RequestParam("query") String query
    ) {
        List<UserGroupPointsDTO> users = userService.searchStudentsByFullName(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getInfoByUserId/{userId}")
    public ResponseEntity<UserMyInfoDTO> getInfoByUserId(@PathVariable Integer userId) {
        try {
            UserMyInfoDTO u = userService.getUserMyInfoDTOByUserId(userId);
            return ResponseEntity.ok(u);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getUserProfileInfo/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfileByUserId(@PathVariable Integer userId) {
        try {
            Optional<UserProfileDTO> u = userService.getUserProfileInfo(userId);
            return ResponseEntity.ok(u.get());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @PutMapping("/updateInfoByUserId/{userId}")
    public ResponseEntity<UserMyInfoDTO> updateUserInfo(
            @PathVariable Integer userId,
            @RequestBody UserMyInfoDTO userDto) {

        UserMyInfoDTO updatedUser = userService.updateUserInfo(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/registerStudent")
    public ResponseEntity<Integer> registerStudent(@RequestBody StudentRegisterDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPatronymic(dto.getPatronymic());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setGroup(dto.getGroupId()); // может быть null
        user.setRole(4); // Роль должна быть в БД

        userRepository.save(user);
        return ResponseEntity.ok(user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginStudentsDTO request) {
        User user = userRepository.findByLogin(request.getLogin());

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }

        StudentProfileDTO studentData = new StudentProfileDTO(userService.getStudentByUserId(user.getId()), user.getRole());
        return ResponseEntity.ok(studentData);
    }

    @GetMapping("/student/{userId}")
    public ResponseEntity<UserProfileDTO> getStudentData(@PathVariable Integer userId) {
        UserProfileDTO student = userService.getStudentByUserId(userId);
        return ResponseEntity.ok(student);
    }

}
