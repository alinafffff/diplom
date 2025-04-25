package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.News;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Integer> {
    List<News> findAllByAuthor(Integer id);
    List <News> findByContentContainingIgnoreCase(String query);
}
