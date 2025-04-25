package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Role;
import ru.diplom.diplom.models.Team;
import ru.diplom.diplom.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    List<User> findByRole(Integer role);
    List<User> findByLoginContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrNameContainingIgnoreCaseOrPatronymicContainingIgnoreCase(String login, String surname, String name, String patronymic);
    List<User> findByGroup(Integer groupId);

}
