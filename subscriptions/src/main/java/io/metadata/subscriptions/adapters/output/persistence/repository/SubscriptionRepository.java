package io.metadata.subscriptions.adapters.output.persistence.repository;

import io.metadata.subscriptions.adapters.output.persistence.entity.SubscriptionEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, Long>
{
    @Query(value="SELECT * FROM subscriptions sub "
        + "INNER JOIN courses c on c.id = sub.course_id and c.deleted = 0 "
        + "INNER JOIN students s on s.id = sub.course_id and s.deleted = 0 "
        + "WHERE c.id = :id")
    Iterable<SubscriptionEntity> findAllByCourseId(@Param("id") Long id);

    @Query(value="SELECT count(1) FROM subscriptions sub "
        + "INNER JOIN courses c on c.id = sub.course_id and c.deleted = 0 "
        + "INNER JOIN students s on s.id = sub.course_id and s.deleted = 0 "
        + "WHERE c.id = :id")
    Long countAllByCourseId(@Param("id") Long id);

    @Query(value="SELECT * FROM subscriptions sub "
        + "INNER JOIN courses c on c.id = sub.course_id and c.deleted = 0 "
        + "INNER JOIN students s on s.id = sub.course_id and s.deleted = 0 "
        + "WHERE s.id = :id")
    Iterable<SubscriptionEntity> findAllByStudentId(@Param("id") Long id);

    @Query(value="SELECT count(1) FROM subscriptions sub "
        + "INNER JOIN courses c on c.id = sub.course_id and c.deleted = 0 "
        + "INNER JOIN students s on s.id = sub.course_id and s.deleted = 0 "
        + "WHERE s.id = :id")
    Long countAllByStudentId(@Param("id") Long id);
}
