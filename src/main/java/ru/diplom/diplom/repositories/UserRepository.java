package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.dto.UserEventShortDTO;
import ru.diplom.diplom.models.Role;
import ru.diplom.diplom.models.Team;
import ru.diplom.diplom.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    List<User> findByRole(Integer role);
    List<User> findByLoginContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrNameContainingIgnoreCaseOrPatronymicContainingIgnoreCase(String login, String surname, String name, String patronymic);
    List<User> findByGroup(Integer groupId);
    @Query("SELECT u FROM User u WHERE u.role = :role ORDER BY u.points DESC")
    List<User> findByRoleOrderedByPointsDesc(@Param("role") int role);

    @Query("""
    SELECT u FROM User u
    WHERE LOWER(CONCAT(u.name, ' ', u.surname, ' ', u.patronymic)) LIKE LOWER(CONCAT('%', :query, '%'))
    AND u.group = :groupId
    """)
    List<User> searchByFullName(@Param("query") String query, @Param("groupId") Integer groupId);

    @Query("""
    SELECT u FROM User u
    WHERE LOWER(CONCAT(u.name, ' ', u.surname, ' ', u.patronymic)) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<User> searchStudentsByFullName(@Param("query") String query);


}
