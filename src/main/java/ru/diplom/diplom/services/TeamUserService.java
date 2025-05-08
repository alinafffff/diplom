package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.UserEventShortDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.TeamUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamUserService {
    @Autowired
    private final TeamUserRepository teamUserRepository;

    public List<UserEventShortDTO> getEventsByStudentId(Integer studentId) {
        return teamUserRepository.findEventsByStudentId(studentId)
                .stream()
                .map(this::convertToUserEventShortDTO)
                .toList();
    }





    public UserEventShortDTO convertToUserEventShortDTO(Event event) {
        return UserEventShortDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}
