package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.GroupEventRepository;

@Service
@RequiredArgsConstructor
public class GroupEventService {
    private final GroupEventRepository groupEventRepository;
}
