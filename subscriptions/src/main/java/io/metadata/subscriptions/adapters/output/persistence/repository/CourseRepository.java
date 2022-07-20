package io.metadata.subscriptions.adapters.output.persistence.repository;

import io.metadata.subscriptions.adapters.output.persistence.entity.CourseEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<CourseEntity, Long>
{
    @Query(value = "SELECT c.* FROM courses c "
        + "LEFT JOIN subscriptions s on c.id = s.course_id "
        + "WHERE c.deleted = 0 "
        + "GROUP BY s.student_id,c.id "
        + "HAVING count(s.student_id) = 0")
    Iterable<CourseEntity> findAllEmpty();

    @Modifying
    @Query(value = "UPDATE courses c SET c.deleted = 1 WHERE c.id = :id")
    Long softDeleteById(@Param("id") Long id);
}
