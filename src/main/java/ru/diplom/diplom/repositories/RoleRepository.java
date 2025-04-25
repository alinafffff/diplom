package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String roleName);
}