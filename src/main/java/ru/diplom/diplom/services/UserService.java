package ru.diplom.diplom.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.models.Role;
import ru.diplom.diplom.repositories.*;

import java.time.LocalDate;
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

    private final ProfileRepository profileRepository;

    private final DirectionRepository directionRepository;

    private final FormRepository formRepository;

    private final LevelRepository levelRepository;

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

    public void createUser(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPatronymic(dto.getPatronymic());
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
                .photoUrl(user.getPhotoUrl())
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


    private int calculateCourse(LocalDate startDate) {
        if (startDate == null) return 0;

        LocalDate now = LocalDate.now();
        int course = now.getYear() - startDate.getYear();
        if (now.getMonthValue() < 9) {
            course -= 1;
        }
        return Math.max(course + 1, 1);
    }

    private boolean isArchived(LocalDate startDate, int duration) {
        LocalDate endOfStudies = startDate.plusYears(duration).withMonth(8).withDayOfMonth(31);
        return LocalDate.now().isAfter(endOfStudies);
    }

    public GroupDTO convertToGroupDTO(Group g) {
        String directionAbbreviation;
        String n;

        if (g.getProfile() != null) {
            directionAbbreviation = profileRepository.findById(g.getProfile())
                    .flatMap(p -> directionRepository.findById(p.getDirection()))
                    .map(Direction::getAbbreviation)
                    .orElse("Not found direction from profile");
        } else {
            directionAbbreviation = directionRepository.findById(g.getDirection())
                    .map(Direction::getAbbreviation)
                    .orElse("Not found");
        }

        String formName = formRepository.findById(g.getForm())
                .map(Form::getAbbreviation)
                .orElse("");

        String levelName = levelRepository.findById(g.getMyLevel())
                .map(Level::getAbbreviation)
                .orElse("");

        Integer profileNumber = null;
        if (g.getProfile() != null) {
            profileNumber = profileRepository.findById(g.getProfile())
                    .map(Profile::getNumber)
                    .orElse(null);
        }

        int course = calculateCourse(g.getStartDate());
        boolean archived = isArchived(g.getStartDate(), g.getDuration());

        if (profileNumber == null){
            n = levelName+"-"+directionAbbreviation+formName+"-"+course+g.getNumber();
        }
        else n = levelName+profileNumber+"-"+directionAbbreviation+formName+"-"+course+g.getNumber();

        return GroupDTO.builder()
                .id(g.getId())
                .name(n)
                .build();
    }


    public UserProfileDTO getStudentByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Group group = null;
        if (user.getGroup() != null) {
            group = groupRepository.findById(user.getGroup()).orElse(null);
        }

        return UserProfileDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .email(user.getEmail())
                .phone(user.getPhone())
                .points(user.getPoints())
                .groupId(group != null ? group.getId() : null)
                .groupName(group != null ? convertToGroupDTO(group).getName() : null)
                .build();
    }

}