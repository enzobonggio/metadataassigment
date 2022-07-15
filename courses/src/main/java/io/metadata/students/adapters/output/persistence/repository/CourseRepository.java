package io.metadata.students.adapters.output.persistence.repository;

import io.metadata.students.adapters.output.persistence.entity.CourseEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<CourseEntity, Long>
{
    @Modifying
    @Query(value="UPDATE COURSES s SET s.STATE = :state WHERE s.ID = :id")
    Long updateState(@Param("id") Long id, @Param("state") String state);
}
