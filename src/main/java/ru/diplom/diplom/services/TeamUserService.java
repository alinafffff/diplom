package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.*;
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
    private final UserService userService;

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

    @Transactional
    public void removeTeamFromEvent(Integer teamId, Integer eventId) {
        teamUserRepository.deleteUsersFromTeam(teamId);
        teamUserRepository.deleteTeamById(teamId);
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

    @Transactional
    public void closeHackathon(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Team> teams = teamRepository.findByMyEvent(eventId);

        for (Team team : teams) {
            awardPointsToTeamMembers(team);
        }

        LocalDateTime nowMinusOneHour = LocalDateTime.now().minusHours(1);

        if (event.getEndDate() != null) {
            event.setEndDate(nowMinusOneHour);
        } else {
            event.setStartDate(nowMinusOneHour);
        }

        eventRepository.save(event);
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
            case 1:
                return event.getPoints1st();
            case 2:
                return event.getPoints2nd();
            case 3:
                return event.getPoints3rd();
            default:
                return event.getPointsParticipation();
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

    public List<TeamEventDTO> searchTeamsByNameAndEvent(String name, Integer eventId) {
        List<Team> teams = teamRepository.findByNameContainingIgnoreCaseAndMyEvent(name, eventId);
        return teams.stream()
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

    @Transactional
    public TeamEventDTO createTeamForOne(Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
        // Создание новой команды
        Team team = new Team();
        team.setName(String.valueOf(userRepository.findById(userId).orElseThrow().getName()));
        team.setMyEvent(eventId);
        team.setIsConfirmed(true); // или true, если не требуется подтверждение
        team = teamRepository.save(team);
        // Добавление первого участника
        TeamUser teamUser = new TeamUser();
        teamUser.setUser(userId);
        teamUser.setTeam(team.getId());
        teamUserRepository.save(teamUser);
        return convertToTeamEventDTO(team);
    }

    @Transactional
    public TeamEventDTO createTeam(Integer eventId, Integer userId, String teamName) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));
        // Создание новой команды
        Team team = new Team();
        team.setName(teamName);
        team.setMyEvent(eventId);
        team.setIsConfirmed(true); // или true, если не требуется подтверждение
        team = teamRepository.save(team);
        // Добавление первого участника
        TeamUser teamUser = new TeamUser();
        teamUser.setUser(userId);
        teamUser.setTeam(team.getId());
        teamUserRepository.save(teamUser);
        return convertToTeamEventDTO(team);
    }

    public boolean joinTeam(Integer teamId, Integer userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Команда не найдена"));

        // Проверка — не в другой ли команде уже этот пользователь на этом мероприятии
        List<Team> allTeams = teamRepository.findAllByMyEvent(team.getMyEvent());
        for (Team t : allTeams) {
            if (teamUserRepository.existsByTeamIdAndUserId(t.getId(), userId)) {
                return false; // пользователь уже в другой команде на этом мероприятии
            }
        }

        // Проверка — не превышен ли лимит участников
        int currentSize = teamUserRepository.countByTeamId(teamId);
        Event event = eventRepository.findById(team.getMyEvent())
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));

        if (currentSize >= event.getMaxTeamSize()) {
            return false; // команда уже полная
        }

        // Добавляем участника
        TeamUser teamUser = new TeamUser();
        teamUser.setTeam(teamId);
        teamUser.setUser(userId);
        teamUserRepository.save(teamUser);
        return true;
    }

    public void distributePointsToAllParticipants(Integer eventId) {
        EventHackathonDTO event = convertToHackathonDTO(eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено")));

        List<Team> teams = teamRepository.findAllByMyEvent(eventId);

        for (Team team : teams) {
            Integer place = team.getPlace(); // 1, 2, 3 или null
            Integer pointsToGive;

            if (place != null) {
                switch (place) {
                    case 1 -> pointsToGive = event.getPoints1st();
                    case 2 -> pointsToGive = event.getPoints2nd();
                    case 3 -> pointsToGive = event.getPoints3rd();
                    default -> pointsToGive = event.getPointsParticipation();
                }
            } else {
                pointsToGive = event.getPointsParticipation();
            }

            List<User> participants = teamUserRepository.findUsersByTeamId(team.getId());

            for (User u : participants) {
                User user = userRepository.findById(u.getId())
                        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
                user.setPoints(user.getPoints() + pointsToGive); // или updateScore(userId, pointsToGive)
                userRepository.save(user);
            }
        }
    }

    private EventHackathonDTO convertToHackathonDTO(Event e) {
        User author = null;

        if (e.getCreatedBy() != null) {
            author = userRepository.findById(e.getCreatedBy()).orElse(null);
        }

        String authorFullName = (author != null)
                ? author.getSurname() + " " + author.getName() + " " + author.getPatronymic()
                : null;

        return new EventHackathonDTO(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getStartDate(),
                e.getEndDate(),
                e.getType(),
                authorFullName,
                e.getPoints1st(),
                e.getPoints2nd(),
                e.getPoints3rd(),
                e.getPointsParticipation(),
                e.getPhotoUrl(),
                e.getMaxParticipants(),
                e.getMaxTeamSize(),
                e.getIsStudentCouncilRequest(),
                e.getIsRejected());
    }


    public TeamDTO getTeammatesFromEventAndUserId(Integer userId, Integer eventId) {
        Integer teamId = teamRepository.findIdByEventIdAndMemberId(userId, eventId).orElseThrow();
        String teamName = teamRepository.getNameById(teamId);
        Integer groupId = userRepository.findById(userId).orElseThrow().getGroup();
        List<TeamMateDTO> members = userRepository.findTeamMembersIdByTeamId(teamId)
                .stream()
                .map(user -> new TeamMateDTO(user.getId(),
                        user.getSurname(),
                        user.getName(),
                        user.getPatronymic(),
                        userService.convertToGroupDTO(groupRepository.findByMemberId(user.getId())
                                        .orElseThrow())
                                .getName()))
                .toList();
        return new TeamDTO(teamId, teamName, members);
    }

    public TeamEventDTO getTeamForAch(Integer userId, Integer eventId) {
        Integer teamId = teamRepository.findIdByEventIdAndMemberId(userId, eventId).orElseThrow();
        String teamName = teamRepository.getNameById(teamId);
        Integer place = teamRepository.findById(teamId).orElseThrow().getPlace();
        String diploma = teamRepository.findById(teamId).orElseThrow().getDiploma();
        Integer groupId = userRepository.findById(userId).orElseThrow().getGroup();
        boolean bool = teamRepository.findById(teamId).orElseThrow().getIsConfirmed();
        return new TeamEventDTO(teamId, teamName,"", place, diploma, eventId, bool);
    }

    public void updateTeam(TeamUpdateDTO dto) {

            Team team = teamRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Команда не найдена: " + dto.getId()));
            team.setPlace(dto.getPlace());
            team.setDiploma(dto.getDiploma());
            teamRepository.save(team);

    }
}
