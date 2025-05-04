package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.diplom.diplom.dto.SaveEventDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.EventType;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.EventRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private final EventRepository eventRepository;

    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }

    public List<Event> findByType(EventType type) {
        return eventRepository.findByType(type);
    }

    public Event save(SaveEventDTO saveEventDto) {
        return eventRepository.save(saveEventDto.toEntity());
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
}
