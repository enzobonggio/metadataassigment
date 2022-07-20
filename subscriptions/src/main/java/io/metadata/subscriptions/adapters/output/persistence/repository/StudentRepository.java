package io.metadata.subscriptions.adapters.output.persistence.repository;

import io.metadata.subscriptions.adapters.output.persistence.entity.StudentEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long>
{
    @Query(value = "SELECT s.* FROM students s "
        + "LEFT JOIN subscriptions sub on s.id = sub.student_id "
        + "WHERE s.deleted = 0 "
        + "GROUP BY sub.course_id,s.id "
        + "HAVING count(sub.course_id) = 0")
    Iterable<StudentEntity> findAllLazy();

    @Modifying
    @Query(value = "UPDATE students s SET s.deleted = 1 WHERE s.id = :id")
    Long softDeleteById(@Param("id") Long id);
}
