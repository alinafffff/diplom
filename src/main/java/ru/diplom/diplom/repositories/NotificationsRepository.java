package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.models.NotificationType;
import ru.diplom.diplom.models.Notifications;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Integer> {

    List<Notifications> findByTextContainingIgnoreCase(String text);
    List<Notifications> findByTypeInOrderByCreatedAtDesc(List<NotificationType> types);

    @Query("SELECT n FROM Notifications n " +
            "WHERE n.type IN :types " +
            "AND LOWER(n.text) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY n.createdAt DESC")
    List<Notifications> findByTypeInAndTextContainingIgnoreCase(
            @Param("types") List<NotificationType> types,
            @Param("query") String query);

}