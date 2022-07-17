package io.metadata.courses.adapters.output.persistence.repository;

import io.metadata.courses.adapters.output.persistence.entity.CourseEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<CourseEntity, Long>
{
    @Modifying
    @Query(value="UPDATE courses s SET s.state = :state WHERE s.id = :id")
    Long updateState(@Param("id") Long id, @Param("state") String state);

    @Modifying
    @Query(value="UPDATE courses s SET s.name = :name WHERE s.id = :id")
    Long update(@Param("id") Long id, @Param("name") String name);
}
