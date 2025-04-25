package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.models.Direction;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.LevelRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LevelService {
    private final LevelRepository levelRepository;

    public List<Level> getAllLevels(){
        return levelRepository.findAll();
    }

    @Transactional
    public List<Level> searchLevels(String query) {
        List<Level> l;
        l = levelRepository.findByNameContainingIgnoreCase(query);
        return l;
    }

    public Optional<Level> getLevelById(Integer id) {
        return levelRepository.findById(id);
    }

    public Level updateLevel(Integer id, Level updatedLevel) {
        return levelRepository.findById(id).map(l -> {
            l.setName(updatedLevel.getName());
            l.setAbbreviation(updatedLevel.getAbbreviation());
            return levelRepository.save(l);
        }).orElseThrow(() -> new RuntimeException("Уровень с id " + id + " не найден"));
    }

    public Level createLevel(Level l) {
        return levelRepository.save(l);
    }
}
