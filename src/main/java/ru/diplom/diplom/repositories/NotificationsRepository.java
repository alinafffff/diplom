package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Notifications;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Integer> {
}