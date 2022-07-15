package io.metadata.subscriptions.adapters.output.persistence.repository;

import io.metadata.subscriptions.adapters.output.persistence.entity.SubscriptionEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long>
{
    @Query(value="SELECT * FROM SUBSCRIPTIONS s WHERE s.COURSE_ID = :id")
    Iterable<SubscriptionEntity> findAllByCourseId(@Param("id") Long id);

    @Query(value="SELECT * FROM SUBSCRIPTIONS s WHERE s.STUDENT_ID = :id")
    Iterable<SubscriptionEntity> findAllByStudentId(@Param("id") Long id);
}
