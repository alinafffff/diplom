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

    @Query(value = "SELECT t.id FROM team_my_user tu JOIN team t ON tu.team_id = t.id " +
            "WHERE tu.my_user_id = ?1 AND t.my_event_id = ?2 ORDER BY t.id ASC LIMIT 1",
            nativeQuery = true)
    Optional<Integer> findIdByEventIdAndMemberId(Integer userId, Integer eventId);

    @Query("select t.name from Team t where t.id = ?1")
    String getNameById(Integer teamId);
}
