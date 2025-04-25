package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.UserAdminDTO;
import ru.diplom.diplom.models.Direction;
import ru.diplom.diplom.models.Role;
import ru.diplom.diplom.models.User;
import ru.diplom.diplom.repositories.DirectionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectionService {
    private final DirectionRepository directionRepository;

    public List<Direction> getAllDirections(){
        return directionRepository.findAll();
    }

    @Transactional
    public List<Direction> searchDirections(String query) {
        List<Direction> d;
        d = directionRepository.findByNameContainingIgnoreCase(query);
        return d;
    }

    public Optional<Direction> getDirectionById(Integer id) {
        return directionRepository.findById(id);
    }

    public Direction updateDirection(Integer id, Direction updatedDirection) {
        return directionRepository.findById(id).map(direction -> {
            direction.setName(updatedDirection.getName());
            direction.setAbbreviation(updatedDirection.getAbbreviation());
            return directionRepository.save(direction);
        }).orElseThrow(() -> new RuntimeException("Направление с id " + id + " не найдено"));
    }

    public Direction createDirection(Direction d) {
        return directionRepository.save(d);
    }
}
