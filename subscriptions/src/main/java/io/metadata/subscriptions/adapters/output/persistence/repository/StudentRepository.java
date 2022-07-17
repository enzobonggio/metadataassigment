package io.metadata.subscriptions.adapters.output.persistence.repository;

import io.metadata.subscriptions.adapters.output.persistence.entity.StudentEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long>
{
    @Query(value = "SELECT s.* FROM students s "
        + "LEFT JOIN subscriptions sub on s.id = sub.student_id "
        + "GROUP BY sub.course_id,s.id "
        + "HAVING count(sub.course_id) = 0")
    Iterable<StudentEntity> findAllLazy();
}
