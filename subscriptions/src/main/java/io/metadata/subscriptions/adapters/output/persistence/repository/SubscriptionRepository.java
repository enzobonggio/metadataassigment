package io.metadata.subscriptions.adapters.output.persistence.repository;

import io.metadata.subscriptions.adapters.output.persistence.entity.SubscriptionEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long>
{
    @Query(value="SELECT * FROM subscriptions s WHERE s.course_id = :id")
    Iterable<SubscriptionEntity> findAllByCourseId(@Param("id") Long id);

    @Query(value="SELECT count(1) FROM subscriptions s WHERE s.course_id = :id")
    Long countAllByCourseId(@Param("id") Long id);

    @Query(value="SELECT * FROM subscriptions s WHERE s.student_id = :id")
    Iterable<SubscriptionEntity> findAllByStudentId(@Param("id") Long id);

    @Query(value="SELECT count(1) FROM subscriptions s WHERE s.student_id = :id")
    Long countAllByStudentId(@Param("id") Long id);
}
