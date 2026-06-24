package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.repositories.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NewsRepository newsRepository;
    private final NewsService newsService;
    @Autowired
    private final TeamUserRepository teamUserRepository;

    private final TeamRepository teamRepository;

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
    public EventVolunteeringDTO updateVolunteeringEvent(Integer eventId, EventVolunteeringDTO dto) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));

        if (!EventType.волонтерство.equals(existingEvent.getType())) {
            throw new RuntimeException("Можно обновлять только волонтерские мероприятия");
        }

        if (dto.getName() != null) {
            existingEvent.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existingEvent.setDescription(dto.getDescription());
        }
        if (dto.getStartDate() != null) {
            existingEvent.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            existingEvent.setEndDate(dto.getEndDate());
        }
        if (dto.getPoints() != null) {
            existingEvent.setPoints(dto.getPoints());
        }
        if (dto.getPhotoUrl() != null) {
            existingEvent.setPhotoUrl(dto.getPhotoUrl());
        }
        if (dto.getMaxParticipants() != null) {
            existingEvent.setMaxParticipants(dto.getMaxParticipants());
        }

        Event updatedEvent = eventRepository.save(existingEvent);

        return convertToVolunteeringDTO(updatedEvent);
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
    public EventHackathonDTO updateHackathonEvent(Integer eventId, EventHackathonDTO dto) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Хакатон не найден"));

        if (!EventType.хакатон.equals(existingEvent.getType())) {
            throw new RuntimeException("Можно обновлять только хакатоны");
        }

        if (dto.getName() != null) {
            existingEvent.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existingEvent.setDescription(dto.getDescription());
        }
        if (dto.getStartDate() != null) {
            existingEvent.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            existingEvent.setEndDate(dto.getEndDate());
        }
        if (dto.getPoints1st() != null) {
            existingEvent.setPoints1st(dto.getPoints1st());
        }
        if (dto.getPoints2nd() != null) {
            existingEvent.setPoints2nd(dto.getPoints2nd());
        }
        if (dto.getPoints3rd() != null) {
            existingEvent.setPoints3rd(dto.getPoints3rd());
        }
        if (dto.getPointsParticipation() != null) {
            existingEvent.setPointsParticipation(dto.getPointsParticipation());
        }
        if (dto.getPhotoUrl() != null) {
            existingEvent.setPhotoUrl(dto.getPhotoUrl());
        }
        if (dto.getMaxParticipants() != null) {
            existingEvent.setMaxParticipants(dto.getMaxParticipants());
        }
        if (dto.getMaxTeamSize() != null) {
            existingEvent.setMaxTeamSize(dto.getMaxTeamSize());
        }

        Event updatedEvent = eventRepository.save(existingEvent);

        return convertToHackathonDTO(updatedEvent);
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
                .registrationLink(dto.getRegistrationLink())
                .build();

        Event savedEvent = eventRepository.save(event);
        return convertToPartnersHackathonDTO(savedEvent);
    }

    @Transactional
    public EventPartnersHackathonDTO updatePartnersHackathonEvent(Integer eventId, EventPartnersHackathonDTO dto) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Хакатон от партнера не найден"));

        if (!EventType.хакатон_от_партнера.equals(existingEvent.getType())) {
            throw new RuntimeException("Можно обновлять только хакатоны от партнера");
        }

        if (dto.getName() != null) {
            existingEvent.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existingEvent.setDescription(dto.getDescription());
        }
        if (dto.getStartDate() != null) {
            existingEvent.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            existingEvent.setEndDate(dto.getEndDate());
        }
        if (dto.getPoints1st() != null) {
            existingEvent.setPoints1st(dto.getPoints1st());
        }
        if (dto.getPoints2nd() != null) {
            existingEvent.setPoints2nd(dto.getPoints2nd());
        }
        if (dto.getPoints3rd() != null) {
            existingEvent.setPoints3rd(dto.getPoints3rd());
        }
        if (dto.getPointsParticipation() != null) {
            existingEvent.setPointsParticipation(dto.getPointsParticipation());
        }
        if (dto.getPhotoUrl() != null) {
            existingEvent.setPhotoUrl(dto.getPhotoUrl());
        }
        if (dto.getRegistrationLink() != null) {
            existingEvent.setRegistrationLink(dto.getRegistrationLink());
        }

        Event updatedEvent = eventRepository.save(existingEvent);

        return convertToPartnersHackathonDTO(updatedEvent);
    }


    public List<?> getAllEventsWithTypeSpecificDTOs() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> {
                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime startDate = event.getStartDate();
                    LocalDateTime effectiveEndDate = event.getEndDate() != null
                            ? event.getEndDate()
                            : startDate.plusDays(1);

                    boolean isUpcoming = event.getStartDate().isAfter(now);
                    boolean isOngoing = !event.getStartDate().isAfter(now) &&
                            (!effectiveEndDate.isBefore(now));

                    boolean dateCondition = isUpcoming || isOngoing;
                    boolean statusCondition = Boolean.FALSE.equals(isStudentCouncilRequest) ||
                            (Boolean.TRUE.equals(isStudentCouncilRequest) && Boolean.FALSE.equals(isRejected));

                    return dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public Object getHackathonById(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getType() != EventType.хакатон && event.getType() != EventType.хакатон_от_партнера) {
            throw new RuntimeException("Event is not a hackathon");
        }

        return convertToSpecificDTO(event);
    }


    public List<?> getAllArchiveEventsWithTypeSpecificDTOs() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> {
                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime date = event.getStartDate();
                    if(event.getEndDate()!= null){
                        date = event.getEndDate();
                    }

                    boolean dateCondition = date != null && date.isBefore(now);
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
                    LocalDateTime effectiveEndDate = event.getEndDate() != null
                            ? event.getEndDate()
                            : startDate.plusDays(1);

                    boolean isUpcoming = event.getStartDate().isAfter(now);
                    boolean isOngoing = !event.getStartDate().isAfter(now) &&
                            (!effectiveEndDate.isBefore(now));

                    boolean dateCondition = isUpcoming || isOngoing;
                    boolean statusCondition = Boolean.FALSE.equals(isStudentCouncilRequest) ||
                            (Boolean.TRUE.equals(isStudentCouncilRequest) &&
                                    Boolean.FALSE.equals(isRejected));

                    return correctType && dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public List<?> getFilteredArchivedEventsByTypes(List<String> eventTypes) {
        LocalDateTime now = LocalDateTime.now();

        return eventRepository.findAll().stream()
                .filter(event -> {
                    boolean correctType = eventTypes.stream()
                            .anyMatch(type -> type.equalsIgnoreCase(event.getType().getName()));

                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime date = event.getStartDate();
                    if(event.getEndDate()!= null){
                        date = event.getEndDate();
                    }
                    System.out.println("Сравниваем: " + date + " (мероприятие) vs " + now + " (сейчас)");

                    boolean dateCondition = date != null && date.isBefore(now);
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

                    boolean isUpcoming = event.getStartDate().isAfter(now);
                    boolean isOngoing = !event.getStartDate().isAfter(now) &&
                            (event.getEndDate() == null || !event.getEndDate().isBefore(now));

                    boolean dateCondition = isUpcoming || isOngoing;
                    boolean statusCondition = Boolean.TRUE.equals(isStudentCouncilRequest)
                            && Boolean.FALSE.equals(isRejected);

                    return dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public List<?> getStudentCouncilRequestArchivedEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> {
                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime date = event.getStartDate();
                    if(event.getEndDate()!= null){
                        date = event.getEndDate();
                    }

                    boolean dateCondition = date != null && date.isBefore(now);
                    boolean statusCondition = Boolean.TRUE.equals(isStudentCouncilRequest)
                            && Boolean.FALSE.equals(isRejected);

                    return dateCondition && statusCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }


    public List<?> getAMyEvents(Integer myId) {
        LocalDateTime now = LocalDateTime.now();
        List<Event> eventsList = eventRepository.findAllByCreatedBy(myId);
        return eventsList.stream()
                .map(this::convertToSpecificDTO)
                .toList();
    }

    public List<?> getEventStudsovetRequests() {
        String roleName = "студсовет";
        return eventRepository.findAll().stream()
                .filter(event -> {
                    boolean isMatchingRole = userRepository.findById(event.getCreatedBy())
                            .flatMap(user -> roleRepository.findById(user.getRole()))
                            .map(role -> role.getName().equalsIgnoreCase(roleName))
                            .orElse(false);

                    // 2. Фильтрация по статусу студсовета
                    boolean condition = Boolean.TRUE.equals(event.getIsStudentCouncilRequest())
                            && (event.getIsRejected()==null);

                    return isMatchingRole && condition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public List<?> getEventStudsovetRejectedRequests() {
        String roleName = "студсовет";
        return eventRepository.findAll().stream()
                .filter(event -> {
                    boolean isMatchingRole = userRepository.findById(event.getCreatedBy())
                            .flatMap(user -> roleRepository.findById(user.getRole()))
                            .map(role -> role.getName().equalsIgnoreCase(roleName))
                            .orElse(false);

                    // 2. Фильтрация по статусу студсовета
                    boolean condition = Boolean.TRUE.equals(event.getIsStudentCouncilRequest())
                            && Boolean.TRUE.equals(event.getIsRejected());

                    return isMatchingRole && condition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
    }

    public List<?> getAllMyArchivedEvents(Integer myId) {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAllByCreatedBy(myId)
                .stream()
                .filter(event -> {
                    LocalDateTime date = event.getStartDate();
                    if(event.getEndDate()!= null){
                        date = event.getEndDate();
                    }
                    boolean dateCondition = date != null && date.isBefore(now);
                    return dateCondition;
                })
                .map(this::convertToSpecificDTO)
                .collect(Collectors.toList());
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

                    boolean isUpcoming = event.getStartDate().isAfter(now);
                    boolean isOngoing = !event.getStartDate().isAfter(now) &&
                            (event.getEndDate() == null || !event.getEndDate().isBefore(now));

                    boolean dateCondition = isUpcoming || isOngoing;
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

    @Transactional
    public List<?> searchArchivedEvents(String query, Integer myId, String filter) {
        LocalDateTime now = LocalDateTime.now();

        return eventRepository.findByDescriptionContainingIgnoreCase(query).stream()
                .filter(event -> {
                    Boolean isStudentCouncilRequest = event.getIsStudentCouncilRequest();
                    Boolean isRejected = event.getIsRejected();
                    LocalDateTime date = event.getStartDate();
                    if(event.getEndDate()!= null){
                        date = event.getEndDate();
                    }

                    boolean dateCondition = date != null && date.isBefore(now);
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

    @Transactional
    public List<?> searchByFilter(String query, String filter) {
        String roleName = "студсовет";
        switch (filter.toLowerCase()) {
            case "новости":
                return newsRepository.findByContentContainingIgnoreCase(query).stream()
                        .filter(news -> {
                            boolean isMatchingRole = userRepository.findById(news.getAuthor())
                                    .flatMap(user -> roleRepository.findById(user.getRole()))
                                    .map(role -> role.getName().equalsIgnoreCase(roleName))
                                    .orElse(false);
                            boolean condition = Boolean.TRUE.equals(news.getIsStudentCouncilRequest())
                                    && (news.getIsRejected() == null);
                            return isMatchingRole && condition;
                        })
                        .map(this::convertToNewsDTO)
                        .collect(Collectors.toList());

            case "мероприятия":
                return eventRepository.findByDescriptionContainingIgnoreCase(query).stream()
                        .filter(event -> {
                            boolean isMatchingRole = userRepository.findById(event.getCreatedBy())
                                    .flatMap(user -> roleRepository.findById(user.getRole()))
                                    .map(role -> role.getName().equalsIgnoreCase(roleName))
                                    .orElse(false);
                            boolean condition = Boolean.TRUE.equals(event.getIsStudentCouncilRequest())
                                    && (event.getIsRejected() == null);
                            return isMatchingRole && condition;
                        })
                        .map(this::convertToSpecificDTO)
                        .collect(Collectors.toList());

            case "отклоненные новости":
                return newsRepository.findByContentContainingIgnoreCase(query).stream()
                        .filter(news -> {
                            boolean isMatchingRole = userRepository.findById(news.getAuthor())
                                    .flatMap(user -> roleRepository.findById(user.getRole()))
                                    .map(role -> role.getName().equalsIgnoreCase(roleName))
                                    .orElse(false);
                            boolean condition = Boolean.TRUE.equals(news.getIsStudentCouncilRequest())
                                    && Boolean.TRUE.equals(news.getIsRejected());
                            return isMatchingRole && condition;
                        })
                        .map(this::convertToNewsDTO)
                        .collect(Collectors.toList());

            case "отклоненные мероприятия":
                return eventRepository.findByDescriptionContainingIgnoreCase(query).stream()
                        .filter(event -> {
                            boolean isMatchingRole = userRepository.findById(event.getCreatedBy())
                                    .flatMap(user -> roleRepository.findById(user.getRole()))
                                    .map(role -> role.getName().equalsIgnoreCase(roleName))
                                    .orElse(false);
                            boolean condition = Boolean.TRUE.equals(event.getIsStudentCouncilRequest())
                                    && Boolean.TRUE.equals(event.getIsRejected());
                            return isMatchingRole && condition;
                        })
                        .map(this::convertToSpecificDTO)
                        .collect(Collectors.toList());

            default:
                return Collections.emptyList();
        }
    }

    public boolean confirmStudsovetRequest(Integer eventId) {
        Optional<Event> dto = eventRepository.findById(eventId);
        if (dto.isPresent()) {
            Event updated = dto.get();
            updated.setIsRejected(false);
            eventRepository.save(updated);
            return true;
        }
        return false;
    }

    public boolean rejectStudsovetRequest(Integer eventId) {
        Optional<Event> dto = eventRepository.findById(eventId);
        if (dto.isPresent()) {
            Event updated = dto.get();
            updated.setIsRejected(true);
            eventRepository.save(updated);
            return true;
        }
        return false;
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
                e.getIsRejected(),
                e.getRegistrationLink());
    }

    private NewsDTO convertToNewsDTO(News news){

        String authorRoleName = userRepository.findById(news.getAuthor())
                .flatMap(user -> roleRepository.findById(user.getRole()))
                .map(Role::getName)
                .orElse("Неизвестная роль");

        return NewsDTO.builder()
                .id(news.getId())
                .createdAt(news.getCreatedAt() != null ? news.getCreatedAt() : LocalDateTime.now())
                .author(news.getAuthor())
                .authorRole(authorRoleName)
                .title(news.getTitle())
                .description(news.getContent())
                .photoUrl(news.getPhotoUrl())
                .build();
    }

    public List<?> getEventsByUserIdAndTypes(Integer userId, List<String> types) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Юзер не надйен"));
        List<TeamUser> teamUserList = teamUserRepository.findAllByUser(user.getId());
        teamUserList.forEach(e -> System.out.println(e.getId()));
        List<Team> teams = teamRepository.findAllById(teamUserList.stream().map(TeamUser::getTeam).toList());
        teams.forEach(e -> System.out.println(e.getId()));
        List<Event> events = eventRepository.findAllById(teams.stream().map(Team::getMyEvent).toList())
                .stream()
                .filter(e->types.contains(e.getType().getName()))
                .toList();
        events.forEach(e -> System.out.println(e.getId()));
        return events.stream().map(this::convertToSpecificDTO).toList();
    }

    @Transactional
    public EventVolunteeringDTO updateMobileVolunteeringEvent(Integer eventId, EventVolunteeringDTO dto) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));

        if (!EventType.волонтерство.equals(existingEvent.getType())) {
            throw new RuntimeException("Можно обновлять только волонтерские мероприятия");
        }

        if (dto.getName() != null) {
            existingEvent.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existingEvent.setDescription(dto.getDescription());
        }
        if (dto.getStartDate() != null) {
            existingEvent.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            existingEvent.setEndDate(dto.getEndDate());
        }
        if (dto.getPoints() != null) {
            existingEvent.setPoints(dto.getPoints());
        }
        if (dto.getPhotoUrl() != null) {
            existingEvent.setPhotoUrl(dto.getPhotoUrl());
        }
        if (dto.getPhotoUrl() == null) {
            existingEvent.setPhotoUrl(null);
        }
        if (dto.getMaxParticipants() != null) {
            existingEvent.setMaxParticipants(dto.getMaxParticipants());
        }

        Event updatedEvent = eventRepository.save(existingEvent);
        System.out.println(existingEvent.getPhotoUrl());
        return convertToVolunteeringDTO(updatedEvent);
    }
}
