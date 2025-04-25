package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.models.Profile;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Integer> {

    List<Profile> findByNameContainingIgnoreCase(String name);
    Optional<Profile> findByName(String name);
    Optional<Profile> findByNumber(Integer n);

}
