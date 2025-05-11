package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Team;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team,Integer> {
    List<Team> findAllByMyEvent(Integer eventId);
    List<Team> findByNameContainingIgnoreCaseAndMyEvent(String name, Integer eventId);

}
