package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
}
