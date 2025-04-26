package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.diplom.diplom.dto.UserGroupDTO;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.dto.GroupRequestsDTO;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.GroupRepository;
import ru.diplom.diplom.repositories.GroupRequestsRepository;
import ru.diplom.diplom.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupRequestsService {
    private final GroupRequestsRepository groupRequestsRepository;
    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;


    public List<GroupRequestsDTO> getGroupRequestsByGroupId(Integer groupId) {
        List<GroupRequests> users = groupRequestsRepository.findByGroup(groupId);
        return users.stream()
                .map(this::convertToGroupRequestsDTO)
                .collect(Collectors.toList());
    }

    public void deleteGroupRequestById(@PathVariable Integer requestId){
        groupRequestsRepository.deleteById(requestId);
    }

    @Transactional
    public List<GroupRequestsDTO> searchRequestsByFullName(String query, Integer groupId) {
        return groupRequestsRepository.searchByFullName(query, groupId)
                .stream()
                .map(this::convertToGroupRequestsDTO)
                .collect(Collectors.toList());
    }


    public UserGroupDTO approveRequest(Integer requestId) {
        GroupRequests request = groupRequestsRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        User user = userRepository.findById(request.getUser())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setGroup(request.getGroup());
        userRepository.save(user);

        groupRequestsRepository.deleteById(requestId);

        return userService.convertToUserDTO(user);
    }

    private GroupRequestsDTO convertToGroupRequestsDTO(GroupRequests groupRequest) {
        Optional<User> userOptional = userRepository.findById(groupRequest.getUser());
        String userFullName = "";
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userFullName = user.getSurname() + " " + user.getName() + " " +
                    (user.getPatronymic() != null ? user.getPatronymic() : "");
        }
        String groupName = "Без группы";
        Integer groupId = groupRequest.getGroup();
        if (groupId != null) {
            Optional<Group> groupOptional = groupRepository.findById(groupId);
            if (groupOptional.isPresent()) {
                groupName = groupService.convertToGroupDTO(groupOptional.get()).getName();
            }
        }
        return GroupRequestsDTO.builder()
                .id(groupRequest.getId())
                .fullName(userFullName)
                .groupName(groupName)
                .groupId(groupId)
                .build();
    }


}
