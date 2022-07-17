package io.metadata.students.adapters.output.persistence.repository;

import io.metadata.students.adapters.output.persistence.entity.StudentEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long>
{
    @Modifying
    @Query(value="UPDATE students s SET s.state = :state WHERE s.id = :id")
    Long updateState(@Param("id") Long id, @Param("state") String state);

    @Modifying
    @Query(value="UPDATE students s SET s.name = :name WHERE s.id = :id")
    Long update(@Param("id") Long id, @Param("name") String name);
}
