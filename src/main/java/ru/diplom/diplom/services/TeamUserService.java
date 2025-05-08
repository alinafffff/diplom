package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.TeamEventDTO;
import ru.diplom.diplom.dto.UserEventShortDTO;
import ru.diplom.diplom.dto.UserGroupDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Team;
import ru.diplom.diplom.models.User;
import ru.diplom.diplom.repositories.EventRepository;
import ru.diplom.diplom.repositories.GroupRepository;
import ru.diplom.diplom.repositories.TeamRepository;
import ru.diplom.diplom.repositories.TeamUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamUserService {
    @Autowired
    private final TeamUserRepository teamUserRepository;
    private final EventRepository eventRepository;
    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final TeamRepository teamRepository;

    public List<UserEventShortDTO> getEventsByStudentId(Integer studentId) {
        LocalDateTime now = LocalDateTime.now();

        return teamUserRepository.findEventsByStudentId(studentId)
                .stream()
                .filter(event ->
                        (event.getEndDate() != null && event.getEndDate().isBefore(now)) ||
                                (event.getEndDate() == null && event.getStartDate().isBefore(now))
                )
                .map(this::convertToUserEventShortDTO)
                .toList();
    }

    @Transactional
    public void removeUserFromEvent(Integer userId, Integer eventId) {
        teamUserRepository.deleteByUserIdAndEventId(userId, eventId);

        List<Integer> emptyTeams = teamUserRepository.findEmptyTeamIdsByEvent(eventId);
        for (Integer teamId : emptyTeams) {
            teamUserRepository.deleteTeamById(teamId);
        }
    }


    public List<?> getUsersByEventId(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));

        List<User> users = teamUserRepository.findUsersByEventId(eventId);
        List<Team> teams = teamUserRepository.findTeamsByEventId(eventId);
        if ("волонтерство".equalsIgnoreCase(event.getType().getName())) {
            return users.stream()
                    .map(this::convertToUserGroupDTO)
                    .toList();
        } else if ("хакатон".equalsIgnoreCase(event.getType().getName())) {
            return teams.stream()
                    .map(this::convertToTeamEventDTO)
                    .toList();
        } else {
            throw new RuntimeException("Смотреть на сайте партнера: " + event.getType());
        }
    }





    public UserEventShortDTO convertToUserEventShortDTO(Event event) {
        return UserEventShortDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }

    public TeamEventDTO convertToTeamEventDTO(Team t) {

        Optional<Event> e = eventRepository.findById(t.getMyEvent());
        String eventName = e.get().getName();

        return TeamEventDTO.builder()
                .id(t.getId())
                .teamName(t.getName())
                .eventName(eventName)
                .place(t.getPlace())
                .diploma(t.getDiploma())
                .eventId(t.getMyEvent())
                .isConfirmed(t.getIsConfirmed())
                .build();
    }

    public UserGroupDTO convertToUserGroupDTO(User user) {
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
}
