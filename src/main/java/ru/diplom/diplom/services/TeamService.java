package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
}
