package ru.diplom.diplom.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
    @Query("select n from Event n where n.type = ?1")
    Page<Event> findAllByCategory(Event.Type type, Pageable pageable);

}
