package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Team;
import ru.diplom.diplom.models.TeamUser;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUser,Integer> {
}
