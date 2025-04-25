package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Direction;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Level;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level,Integer> {

    List<Level> findByNameContainingIgnoreCase(String name);
    Optional<Level> findByName(String name);

}
