package ru.diplom.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.diplom.diplom.models.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group,Integer> {

    @Query(value = """
    SELECT g.*
    FROM my_group g
    JOIN direction d ON g.direction_id = d.id
    WHERE LOWER(d.abbreviation) LIKE LOWER(CONCAT('%', :abbreviation, '%'))
      AND CURRENT_DATE <= 
          make_date(
              EXTRACT(YEAR FROM g.start_date)::int + g.duration,
              8, 
              31
          )
    """, nativeQuery = true)
    List<Group> searchByDirectionAbbreviation(@Param("abbreviation") String abbreviation);

    @Query(value = """
    SELECT g.*
    FROM my_group g
    JOIN direction d ON g.direction_id = d.id
    WHERE LOWER(d.abbreviation) LIKE LOWER(CONCAT('%', :abbreviation, '%'))
      AND CURRENT_DATE > 
          make_date(
              EXTRACT(YEAR FROM g.start_date)::int + g.duration,
              8, 
              31
          )
    """, nativeQuery = true)
    List<Group> searchArchivedByDirectionAbbreviation(@Param("abbreviation") String abbreviation);

    List<Group> findByCurator(Integer curator_id);



}
