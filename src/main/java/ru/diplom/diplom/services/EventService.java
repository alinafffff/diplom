package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.repositories.EventRepository;
import ru.diplom.diplom.repositories.RoleRepository;
import ru.diplom.diplom.repositories.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }

    public List<Event> findByType(EventType type) {
        return eventRepository.findByType(type);
    }

    @Transactional
    public EventVolunteeringDTO createVolunteeringEvent( Integer authorId,EventVolunteeringDTO dto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        boolean isStudsovet = false;
        if (author.getRole() != null) {
            Role role = roleRepository.findById(author.getRole())
                    .orElse(null);
            if (role != null && role.getId().equals(5)) {
                isStudsovet = true;
            }
        }

        Event event = Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .type(EventType.волонтерство)
                .createdBy(author.getId())
                .points(dto.getPoints())
                .photoUrl(dto.getPhotoUrl())
                .maxParticipants(dto.getMaxParticipants())
                .isStudentCouncilRequest(isStudsovet)
                .build();

        Event savedEvent = eventRepository.save(event);

        return convertToVolunteeringDTO(savedEvent);
    }

    @Transactional
    public EventHackathonDTO createHackathonEvent( Integer authorId,EventHackathonDTO dto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Event event = Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .type(EventType.хакатон)
                .createdBy(author.getId())
                .points1st(dto.getPoints1st())
                .points2nd(dto.getPoints2nd())
                .points3rd(dto.getPoints3rd())
                .pointsParticipation(dto.getPointsParticipation())
                .photoUrl(dto.getPhotoUrl())
                .maxParticipants(dto.getMaxParticipants())
                .maxTeamSize(dto.getMaxTeamSize())
                .isStudentCouncilRequest(false)
                .build();

        Event savedEvent = eventRepository.save(event);
        return convertToHackathonDTO(savedEvent);
    }

    @Transactional
    public EventPartnersHackathonDTO createPartnersHackathonEvent(Integer authorId, EventPartnersHackathonDTO dto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Event event = Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .type(EventType.хакатон_от_партнера)
                .createdBy(author.getId())
                .points1st(dto.getPoints1st())
                .points2nd(dto.getPoints2nd())
                .points3rd(dto.getPoints3rd())
                .pointsParticipation(dto.getPointsParticipation())
                .photoUrl(dto.getPhotoUrl())
                .isStudentCouncilRequest(false)
                .build();

        Event savedEvent = eventRepository.save(event);
        return convertToPartnersHackathonDTO(savedEvent);
    }


    public List<?> getAllEventsWithTypeSpecificDTOs() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> {
                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime startDate = event.getStartDate();

                    boolean dateCondition = startDate != null && startDate.isAfter(now);
                    boolean statusCondition = Boolean.FALSE.equals(isStudentCouncilRequest) ||
                            (Boolean.TRUE.equals(isStudentCouncilRequest) && Boolean.FALSE.equals(isRejected));

                    return dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public List<?> getFilteredEventsByTypes(List<String> eventTypes) {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> {
                    boolean correctType = eventTypes.stream()
                            .anyMatch(type -> type.equalsIgnoreCase(event.getType().getName()));

                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime startDate = event.getStartDate();

                    boolean dateCondition = startDate != null && startDate.isAfter(now);
                    boolean statusCondition = Boolean.FALSE.equals(isStudentCouncilRequest) ||
                            (Boolean.TRUE.equals(isStudentCouncilRequest) &&
                                    Boolean.FALSE.equals(isRejected));

                    return correctType && dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public List<?> getStudentCouncilRequestEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> {
                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime startDate = event.getStartDate();

                    boolean dateCondition = startDate != null && startDate.isAfter(now);
                    boolean statusCondition = Boolean.TRUE.equals(isStudentCouncilRequest)
                            && Boolean.FALSE.equals(isRejected);

                    return dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public List<?> getAMyEvents(Integer myId) {
        List<Event> eventsList = eventRepository.findAllByCreatedBy(myId);
        return eventsList.stream()
                .map(this::convertToSpecificDTO)
                .toList();
    }

    @Transactional
    public List<?> searchEvents(String query, Integer myId, String filter) {
        LocalDateTime now = LocalDateTime.now();

        return eventRepository.findByDescriptionContainingIgnoreCase(query).stream()
                .filter(event -> {
                    // Общие условия для всех фильтров
                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime startDate = event.getStartDate();

                    boolean dateCondition = startDate != null && startDate.isAfter(now);
                    boolean statusCondition;
                    boolean tmpCondition = Boolean.FALSE.equals(isStudentCouncilRequest)
                            || (Boolean.TRUE.equals(isStudentCouncilRequest)
                            && Boolean.FALSE.equals(isRejected));

                    // Условия в зависимости от фильтра
                    switch (filter) {
                        case "Мои новости":
                            statusCondition = event.getCreatedBy().equals(myId);
                            break;
                        case "Студсовет":
                            statusCondition = Boolean.TRUE.equals(isStudentCouncilRequest)
                                    && Boolean.FALSE.equals(isRejected);
                            break;
                        case "Волонтерство":
                            statusCondition = "волонтерство".equalsIgnoreCase(event.getType().getName())
                            && tmpCondition;
                            break;
                        case "Хакатоны":
                            statusCondition = (("хакатон".equalsIgnoreCase(event.getType().getName())
                                    || "хакатон_от_партнера".equalsIgnoreCase(event.getType().getName())))
                                    && tmpCondition;
                            break;
                        default: // Все мероприятия
                            statusCondition = tmpCondition;
                    }

                    return dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public Event getOne(int id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        return eventOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    public Event delete(int id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            eventRepository.delete(event);
        }
        return event;
    }




    private Object convertToSpecificDTO(Event event) {
        return switch (event.getType()) {
            case волонтерство -> convertToVolunteeringDTO(event);
            case хакатон -> convertToHackathonDTO(event);
            case хакатон_от_партнера ->  convertToPartnersHackathonDTO(event);
            default -> throw new IllegalStateException("Unknown event type: " + event.getType());
        };
    }


    private EventVolunteeringDTO convertToVolunteeringDTO(Event e) {
        User author = null;

        if (e.getCreatedBy() != null) {
            author = userRepository.findById(e.getCreatedBy()).orElse(null);
        }

        String authorFullName = (author != null)
                ? author.getSurname() + " " + author.getName() + " " + author.getPatronymic()
                : null;

        return new EventVolunteeringDTO(
                e.getId(),
                e.getName(),
                e.getDescription(),
                e.getStartDate(),
                e.getEndDate(),
                e.getType(),
                authorFullName,
                e.getPoints(),
                e.getPhotoUrl(),
                e.getMaxParticipants(),
                e.getIsStudentCouncilRequest(),
                e.getIsRejected());
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

    private EventPartnersHackathonDTO convertToPartnersHackathonDTO(Event e) {
        User author = null;

        if (e.getCreatedBy() != null) {
            author = userRepository.findById(e.getCreatedBy()).orElse(null);
        }

        String authorFullName = (author != null)
                ? author.getSurname() + " " + author.getName() + " " + author.getPatronymic()
                : null;

        return new EventPartnersHackathonDTO(
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
                e.getIsStudentCouncilRequest(),
                e.getIsRejected());
    }
}
