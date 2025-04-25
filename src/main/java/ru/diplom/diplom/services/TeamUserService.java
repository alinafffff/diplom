package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.TeamUserRepository;

@Service
@RequiredArgsConstructor
public class TeamUserService {
    private final TeamUserRepository teamUserRepository;
}
