package io.metadata.subscriptions.adapter.output

import io.metadata.subscriptions.adapters.output.persistence.entity.CourseEntity
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.subscriptions.domain.model.CourseId
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort

class InMemoryCoursePersistenceAdapter implements CourseOutputPort {

    private final PersistenceMapper mapper
    private final List<CourseEntity> subscriptions = []

    InMemoryCoursePersistenceAdapter(final PersistenceMapper mapper) {
        this.mapper = mapper
    }


    void clear() {
        subscriptions.clear()
    }

    @Override
    CourseId save(final CourseId id) {
        return null
    }

    @Override
    CourseId delete(final CourseId id) {
        return null
    }

    @Override
    Collection<CourseId> fetchEmpty() {
        return null
    }
}
