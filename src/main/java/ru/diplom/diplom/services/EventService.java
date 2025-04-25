package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.EventRepository;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
}
