package ru.diplom.diplom.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.models.Role;
import ru.diplom.diplom.repositories.EventRepository;
import ru.diplom.diplom.repositories.GroupRepository;
import ru.diplom.diplom.repositories.RoleRepository;
import ru.diplom.diplom.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;
    @Autowired
    private GroupService groupService;

    public User findUserById(Integer id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserAdminDTO> getAllUsersAdmin() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserGroupPointsDTO> getAllUsersPoints() {
        return userRepository.findByRoleOrderedByPointsDesc(4).stream()
                .map(this::convertToUserGroupPointsDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserProfileDTO> getUserProfileInfo(Integer userId){
        return userRepository.findById(userId)
                .map(this::convertToUserProfileDTO);
    }

    public List<UserAdminDTO> getUsersAdminByRole(Integer roleId) {
        return userRepository.findByRole(roleId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public void updateUser(Integer userId, UserUpdateCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());

        Role role = roleRepository.findByName(dto.getRoleName());

        user.setRole(role.getId());
        userRepository.save(user);
    }

    public void createUser(UserUpdateCreateDTO dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());

        Role role = roleRepository.findByName(dto.getRoleName());
        if (role == null) {
            throw new RuntimeException("Роль не найдена");
        }

        user.setRole(role.getId());
        userRepository.save(user);
    }


    public boolean blockUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setIsBlocked(true);
            userRepository.save(updatedUser);
            return true;
        }
        return false;
    }


    public boolean unblockUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setIsBlocked(false);
            userRepository.save(updatedUser);
            return true;
        }
        return false;
    }

    @Transactional
    public List<UserAdminDTO> searchUsers(String query, String roleName) {
        Role r = roleRepository.findByName(roleName);
        List<User> users;

        if ("Все".equals(roleName)) {
            users = userRepository.findByLoginContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrNameContainingIgnoreCaseOrPatronymicContainingIgnoreCase(query, query, query, query);
        } else {
            users = userRepository.findByLoginContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrNameContainingIgnoreCaseOrPatronymicContainingIgnoreCase(query, query, query, query)
                    .stream()
                    .filter(user -> user.getRole().equals(r.getId()))
                    .collect(Collectors.toList());
        }
        System.out.println(users);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserGroupDTO> searchUsersByFullName(String query, Integer groupId) {
        return userRepository.searchByFullName(query, groupId)
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserGroupDTO> searchUsersByFullNameAndEventId(String query, Integer eventId) {
        return userRepository.searchByFullNameAndEventId(query, eventId)
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserGroupPointsDTO> searchStudentsByFullName(String query) {
        return userRepository.searchStudentsByFullName(query)
                .stream()
                .map(this::convertToUserGroupPointsDTO)
                .collect(Collectors.toList());
    }


    public List<UserGroupDTO> getUsersByGroupId(Integer groupId) {
        List<User> users = userRepository.findByGroup(groupId);
        return users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserMyInfoDTO getUserMyInfoDTOByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("не найден"));
        return convertToUserMyInfoDTO(user);
    }

    @Transactional
    public UserMyInfoDTO updateUserInfo(Integer userId, UserMyInfoDTO userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getSurname() != null) {
            user.setSurname(userDto.getSurname());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getPatronymic() != null) {
            user.setPatronymic(userDto.getPatronymic());
        }
        if (userDto.getPhotoUrl() != null) {
            user.setPhotoUrl(userDto.getPhotoUrl());
        }

        User updatedUser = userRepository.save(user);
        return convertToUserMyInfoDTO(updatedUser);
    }

    @Transactional
    public void removeStudentFromGroup(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setGroup(null);
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не найден");
        }
    }



    public UserGroupDTO convertToUserDTO(User user) {
        String fullName = user.getSurname() + " " + user.getName() + " " +
                (user.getPatronymic() != null ? user.getPatronymic() : "");

        String groupName = "Без группы";
        Integer groupId = user.getGroup();

        if (groupId != null) {
            Optional<Group> groupOptional = groupRepository.findById(groupId);
            if (groupOptional.isPresent()) {
                groupName = groupService.convertToGroupDTO(groupOptional.get()).getName();
            }
        }

        return UserGroupDTO.builder()
                .id(user.getId())
                .fullName(fullName)
                .groupName(groupName)
                .groupId(groupId)
                .build();
    }
    public UserGroupPointsDTO convertToUserGroupPointsDTO(User user) {
        String fullName = user.getSurname() + " " + user.getName() + " " +
                (user.getPatronymic() != null ? user.getPatronymic() : "");

        String groupName = "Без группы";
        Integer groupId = user.getGroup();

        if (groupId != null) {
            Optional<Group> groupOptional = groupRepository.findById(groupId);
            if (groupOptional.isPresent()) {
                groupName = groupService.convertToGroupDTO(groupOptional.get()).getName();
            }
        }

        return UserGroupPointsDTO.builder()
                .id(user.getId())
                .fullName(fullName)
                .groupName(groupName)
                .points(user.getPoints())
                .groupId(groupId)
                .build();
    }

    public UserProfileDTO convertToUserProfileDTO(User user) {
        String groupName = "Без группы";
        Integer groupId = user.getGroup();

        if (groupId != null) {
            Optional<Group> groupOptional = groupRepository.findById(groupId);
            if (groupOptional.isPresent()) {
                groupName = groupService.convertToGroupDTO(groupOptional.get()).getName();
            }
        }

        return UserProfileDTO.builder()
                .id(user.getId())
                .surname(user.getSurname())
                .name(user.getName())
                .patronymic(user.getPatronymic())
                .groupName(groupName)
                .phone(user.getPhone())
                .email(user.getEmail())
                .points(user.getPoints())
                .groupId(groupId)
                .build();
    }

    private UserMyInfoDTO convertToUserMyInfoDTO(User user) {
        return new UserMyInfoDTO(user.getId(), user.getEmail(), user.getSurname(), user.getName(), user.getPatronymic(),user.getPhotoUrl());
    }




    private UserAdminDTO convertToDTO(User user) {
        String fullName = user.getSurname() + " " + user.getName() + " " + user.getPatronymic();
        String status = user.getIsBlocked() ? "Заблокирован" : "Активен";
        String roleName = roleRepository.findById(user.getRole()).map(Role::getName).orElse("Неизвестная роль");

        return new UserAdminDTO(user.getId(), user.getLogin(),user.getEmail(), fullName, roleName, status);
    }

    private UserUpdateCreateDTO convertToDTOUpdateCreate(User user) {
        String roleName = roleRepository.findById(user.getRole()).map(Role::getName).orElse("Неизвестная роль");
        return new UserUpdateCreateDTO(user.getId(), user.getLogin(),user.getEmail(), roleName);
    }
}