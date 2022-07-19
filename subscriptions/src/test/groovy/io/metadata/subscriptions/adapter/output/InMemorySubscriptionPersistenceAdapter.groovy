package io.metadata.subscriptions.adapter.output


import io.metadata.subscriptions.adapters.output.persistence.entity.SubscriptionEntity
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.subscriptions.domain.model.CourseId
import io.metadata.subscriptions.domain.model.StudentId
import io.metadata.subscriptions.domain.model.Subscription
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort

class InMemorySubscriptionPersistenceAdapter implements SubscriptionOutputPort {

    private final PersistenceMapper mapper
    private final List<SubscriptionEntity> subscriptions = []

    InMemorySubscriptionPersistenceAdapter(final PersistenceMapper mapper) {
        this.mapper = mapper
    }

    @Override
    Subscription save(final Subscription subscription) {
        def id = subscriptions.size()
        def entity = mapper.domainToEntity(subscription).withId(id)
        subscriptions.add(entity)
        return mapper.entityToDomain(entity)
    }

    @Override
    Long countByStudentId(final StudentId id) {
        return subscriptions.findAll {it.studentId == id.value}.size()
    }

    @Override
    Collection<Subscription> fetchByStudentId(final StudentId id) {
        return subscriptions.findAll {it.studentId == id.value}.collect { mapper.entityToDomain(it)}
    }

    @Override
    Long countByCourseId(final CourseId id) {
        return subscriptions.findAll {it.courseId == id.value}.size()
    }

    @Override
    Collection<Subscription> fetchByCourseId(final CourseId id) {
        return subscriptions.findAll {it.courseId == id.value}.collect { mapper.entityToDomain(it)}
    }

    void clear() {
        subscriptions.clear()
    }
}
