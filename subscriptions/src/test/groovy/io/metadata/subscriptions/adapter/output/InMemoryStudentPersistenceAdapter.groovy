package io.metadata.subscriptions.adapter.output

import io.metadata.subscriptions.adapters.output.persistence.entity.StudentEntity
import io.metadata.subscriptions.adapters.output.persistence.entity.SubscriptionEntity
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.subscriptions.domain.model.CourseId
import io.metadata.subscriptions.domain.model.StudentId
import io.metadata.subscriptions.domain.model.Subscription
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort

class InMemoryStudentPersistenceAdapter implements StudentOutputPort {

    private final PersistenceMapper mapper
    private final List<StudentEntity> subscriptions = []

    InMemoryStudentPersistenceAdapter(final PersistenceMapper mapper) {
        this.mapper = mapper
    }

    @Override
    StudentId save(final StudentId id) {
        return null
    }

    @Override
    StudentId delete(final StudentId id) {
        return null
    }

    @Override
    Collection<StudentId> fetchLazy() {
        return null
    }

    void clear() {
        subscriptions.clear()
    }
}
