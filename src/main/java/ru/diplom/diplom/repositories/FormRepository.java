package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Form;
import ru.diplom.diplom.models.Level;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form,Integer> {

    List<Form> findByNameContainingIgnoreCase(String name);

    Optional<Form> findByName(String name);

}
