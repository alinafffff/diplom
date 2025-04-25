package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team,Integer> {
}
