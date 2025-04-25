package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    GroupService groupService;

    public List<GroupRequestsDTO> getGroupRequestsByGroupId(Integer groupId) {
        List<GroupRequests> users = groupRequestsRepository.findByGroup(groupId);
        return users.stream()
                .map(this::convertToGroupRequestsDTO)
                .collect(Collectors.toList());
    }

    //какиш

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
