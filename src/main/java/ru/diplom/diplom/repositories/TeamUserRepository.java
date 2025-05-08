package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.dto.UserEventShortDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.Team;
import ru.diplom.diplom.models.TeamUser;

import java.util.List;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUser,Integer> {
    @Query(value = """
                SELECT
                e.id,
                e.name,
                e.description,
                e.start_date,
                e.end_date,
                e.type,
                e.created_by,
                e.points,
                e.points_1st,
                e.points_2nd,
                e.points_3rd,
                e.points_participation,
                e.photo_url,
                e.max_participants,
                e.max_team_size,
                e.is_student_council_request,
                e.is_rejected
                FROM team_my_user tmu
                JOIN team t ON tmu.team_id = t.id
                JOIN my_event e ON t.my_event_id = e.id
                WHERE tmu.my_user_id = :studentId;
            """, nativeQuery = true)
    List<Event> findEventsByStudentId(@Param("studentId") Integer studentId);
}
