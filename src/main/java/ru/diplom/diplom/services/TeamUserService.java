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
import ru.diplom.diplom.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamUserService {
    @Autowired
    private final TeamUserRepository teamUserRepository;
    private final EventRepository eventRepository;
    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

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

    public List<TeamEventDTO> getAllPartnerHackathonTeams() {
        return teamUserRepository.findAllPartnerHackathonTeams()
                .stream()
                .collect(Collectors.toMap(
                        Team::getId,
                        this::convertToTeamEventDTO,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    public List<TeamEventDTO> getAllConfirmedPartnerHackathons() {
        return teamUserRepository.findAllConfirmedPartnerHackathons()
                .stream()
                .collect(Collectors.toMap(
                        Team::getId,
                        this::convertToTeamEventDTO,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    @Transactional
    public TeamEventDTO confirmTeam(Integer teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setIsConfirmed(true);
        Team savedTeam = teamRepository.save(team);

        awardPointsToTeamMembers(savedTeam);

        return convertToTeamEventDTO(savedTeam);
    }

    private void awardPointsToTeamMembers(Team team) {
        Event event = eventRepository.findById(team.getMyEvent())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Integer pointsToAdd = determinePointsToAdd(team.getPlace(), event);

        if (pointsToAdd != null && pointsToAdd > 0) {
            List<User> teamMembers = teamUserRepository.findUsersByTeamId(team.getId());

            teamMembers.forEach(user -> {
                user.setPoints((user.getPoints() != null ? user.getPoints() : 0) + pointsToAdd);
                userRepository.save(user);
            });
        }
    }

    private Integer determinePointsToAdd(Integer place, Event event) {
        if (place == null) {
            return event.getPointsParticipation();
        }

        switch (place) {
            case 1: return event.getPoints1st();
            case 2: return event.getPoints2nd();
            case 3: return event.getPoints3rd();
            default: return event.getPointsParticipation();
        }
    }

    @Transactional
    public Team rejectTeam(Integer teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));

        team.setIsConfirmed(false);
        return teamRepository.save(team);
    }

    public void updateTeams(List<TeamEventDTO> teamDtos) {
        for (TeamEventDTO dto : teamDtos) {
            Team team = teamRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Команда не найдена: " + dto.getId()));
            team.setPlace(dto.getPlace());
            team.setDiploma(dto.getDiploma());
            teamRepository.save(team);
        }
    }



    public List<TeamEventDTO> searchUnconfirmedTeams(String query) {
        return teamUserRepository.findUnconfirmedTeamsByName(query)
                .stream()
                .map(this::convertToTeamEventDTO)
                .collect(Collectors.toList());
    }

    public List<TeamEventDTO> searchConfirmedTeams(String query) {
        return teamUserRepository.findConfirmedTeamsByName(query)
                .stream()
                .map(this::convertToTeamEventDTO)
                .collect(Collectors.toList());
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
