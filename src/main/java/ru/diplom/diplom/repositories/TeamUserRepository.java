package ru.diplom.diplom.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.dto.UserEventShortDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.Team;
import ru.diplom.diplom.models.TeamUser;
import ru.diplom.diplom.models.User;

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

    @Query(value = """
    SELECT u.*
    FROM team_my_user tmu
    JOIN my_user u ON tmu.my_user_id = u.id
    JOIN team t ON tmu.team_id = t.id
    WHERE t.my_event_id = :eventId
    """, nativeQuery = true)
    List<User> findUsersByEventId(@Param("eventId") Integer eventId);

    @Query(value = """
    SELECT t.*
    FROM team_my_user tmu
    JOIN my_user u ON tmu.my_user_id = u.id
    JOIN team t ON tmu.team_id = t.id
    WHERE t.my_event_id = :eventId
    """, nativeQuery = true)
    List<Team> findTeamsByEventId(@Param("eventId") Integer eventId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM team_my_user 
        WHERE my_user_id = :userId 
        AND team_id IN (
            SELECT id FROM team WHERE my_event_id = :eventId
        )
        """, nativeQuery = true)
    void deleteByUserIdAndEventId(@Param("userId") Integer userId, @Param("eventId") Integer eventId);

    @Query(value = """
        SELECT t.id FROM team t
        LEFT JOIN team_my_user tmu ON t.id = tmu.team_id
        WHERE tmu.id IS NULL AND t.my_event_id = :eventId
        """, nativeQuery = true)
    List<Integer> findEmptyTeamIdsByEvent(@Param("eventId") Integer eventId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM team WHERE id = :teamId
        """, nativeQuery = true)
    void deleteTeamById(@Param("teamId") Integer teamId);

    @Query(value = """
    SELECT 
        t.id,
        t.name,
        t.my_event_id,
        t.place,
        t.diploma,
        t.is_confirmed

        FROM team_my_user tmu
        JOIN team t ON tmu.team_id = t.id
        JOIN my_event e ON t.my_event_id = e.id
        WHERE e.type = 'хакатон_от_партнера' AND t.is_confirmed IS NULL;
        """, nativeQuery = true)
    List<Team> findAllPartnerHackathonTeams();

    @Query(value = """
        SELECT 
        t.id,
        t.name,
        t.my_event_id,
        t.place,
        t.diploma,
        t.is_confirmed

        FROM team_my_user tmu
        JOIN team t ON tmu.team_id = t.id
        JOIN my_event e ON t.my_event_id = e.id
        WHERE e.type = 'хакатон_от_партнера' AND t.is_confirmed = true;
    """, nativeQuery = true)
    List<Team> findAllConfirmedPartnerHackathons();

}
