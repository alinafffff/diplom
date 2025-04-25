package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.NotificationsRepository;

@Service
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationsRepository notificationsRepository;
}
