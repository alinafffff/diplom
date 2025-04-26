package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.GroupRequests;
import ru.diplom.diplom.models.User;

import java.util.List;

@Repository
public interface GroupRequestsRepository extends JpaRepository<GroupRequests,Integer> {
    List<GroupRequests> findByGroup(Integer groupId);
    @Query("""
    SELECT g FROM GroupRequests g
    JOIN User u ON g.user = u.id
    WHERE LOWER(CONCAT(u.name, ' ', u.surname, ' ', u.patronymic)) LIKE LOWER(CONCAT('%', :query, '%'))
    AND g.group = :groupId
    """)
    List<GroupRequests> searchByFullName(@Param("query") String query, @Param("groupId") Integer groupId);
}
