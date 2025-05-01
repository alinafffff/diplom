package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.models.Notifications;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Integer> {
    List<Notifications> findAllByOrderByCreatedAtDesc();
    List<Notifications> findByTextContainingIgnoreCase(String text);
}