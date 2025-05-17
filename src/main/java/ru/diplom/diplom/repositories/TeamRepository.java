package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Integer> {
    List<Team> findAllByMyEvent(Integer eventId);
    List<Team> findByNameContainingIgnoreCaseAndMyEvent(String name, Integer eventId);

    List<Team> findByMyEvent(Integer eventId);

    @Query("select t.id from TeamUser tu join Team t on tu.team = t.id where tu.user = ?1 and t.myEvent = ?2")
    Optional<Integer> findIdByEventIdAndMemberId(Integer userId, Integer eventId);

    @Query("select t.name from Team t where t.id = ?1")
    String getNameById(Integer teamId);
}
