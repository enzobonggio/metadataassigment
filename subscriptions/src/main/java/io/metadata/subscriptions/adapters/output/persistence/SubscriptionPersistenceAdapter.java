package io.metadata.subscriptions.adapters.output.persistence;

import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.subscriptions.adapters.output.persistence.repository.SubscriptionRepository;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.Student;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.model.Subscription;
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionOutputPort
{
    private final SubscriptionRepository subscriptionRepository;
    private final PersistenceMapper mapper;

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "subscriptions-by-student", key = "{#subscription.studentId.value}"),
            @CacheEvict(value = "subscriptions-by-course", key = "{#subscription.courseId.value}")
        }
    )
    public Subscription save(final Subscription subscription)
    {
        val courseEntity = subscriptionRepository
            .save(mapper.domainToEntity(subscription));
        return mapper.entityToDomain(courseEntity);
    }

    @Override
    @Cacheable(value = "subscriptions-by-student", key = "{#id.value}")
    public Long countByStudentId(final StudentId id)
    {
        return subscriptionRepository.countAllByStudentId(id.getValue());
    }

    @Override
    public Collection<Subscription> fetchByStudentId(final StudentId id)
    {
        val courses = subscriptionRepository.findAllByStudentId(id.getValue());

        return StreamSupport.stream(courses.spliterator(), false)
            .map(mapper::entityToDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "subscriptions-by-course", key = "{#id.value}")
    public Long countByCourseId(final CourseId id)
    {
        return subscriptionRepository.countAllByCourseId(id.getValue());
    }

    @Override
    public Collection<Subscription> fetchByCourseId(final CourseId id)
    {
        val courses = subscriptionRepository.findAllByCourseId(id.getValue());

        return StreamSupport.stream(courses.spliterator(), false)
            .map(mapper::entityToDomain)
            .collect(Collectors.toList());
    }
}
