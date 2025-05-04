package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.EventType;
import ru.diplom.diplom.models.Role;
import ru.diplom.diplom.models.User;
import ru.diplom.diplom.repositories.EventRepository;
import ru.diplom.diplom.repositories.RoleRepository;
import ru.diplom.diplom.repositories.UserRepository;

import java.util.Optional;

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

    public Event save(SaveEventDTO saveEventDto) {
        return eventRepository.save(saveEventDto.toEntity());
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
