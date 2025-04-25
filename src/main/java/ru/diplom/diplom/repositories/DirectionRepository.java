package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Direction;
import ru.diplom.diplom.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectionRepository extends JpaRepository<Direction,Integer> {

    List<Direction> findByNameContainingIgnoreCase(String name);
    Optional<Direction> findByAbbreviation(String abbreviation);
}
