package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.GroupEvent;

@Repository
public interface GroupEventRepository extends JpaRepository<GroupEvent,Integer> {
}