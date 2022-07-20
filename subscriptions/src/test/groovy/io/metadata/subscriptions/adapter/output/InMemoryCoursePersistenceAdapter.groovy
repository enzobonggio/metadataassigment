package io.metadata.subscriptions.adapter.output

import io.metadata.subscriptions.adapters.output.persistence.entity.CourseEntity
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.subscriptions.domain.model.CourseId
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort

class InMemoryCoursePersistenceAdapter implements CourseOutputPort {

    private final PersistenceMapper mapper
    private final Map<Long, CourseEntity> courses = new HashMap<>()

    InMemoryCoursePersistenceAdapter(final PersistenceMapper mapper) {
        this.mapper = mapper
    }

    @Override
    CourseId save(final CourseId id) {
        courses.put(id.value, mapper.domainToEntity(id))
        return id
    }

    @Override
    CourseId delete(final CourseId id) {
        courses.remove(id.value)
        return id
    }

    @Override
    Collection<CourseId> fetchEmpty() {
        return null
    }

    @Override
    Boolean exists(final CourseId id) {
        return courses.containsKey(id.value)
    }

    void clear() {
        courses.clear()
    }
}
