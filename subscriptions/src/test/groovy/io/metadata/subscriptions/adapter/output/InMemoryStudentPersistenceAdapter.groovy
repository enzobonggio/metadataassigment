package io.metadata.subscriptions.adapter.output

import io.metadata.subscriptions.adapters.output.persistence.entity.CourseEntity
import io.metadata.subscriptions.adapters.output.persistence.entity.StudentEntity
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.subscriptions.domain.model.StudentId
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort

class InMemoryStudentPersistenceAdapter implements StudentOutputPort {

    private final PersistenceMapper mapper
    private final Map<Long, StudentEntity> students = new HashMap<>()

    InMemoryStudentPersistenceAdapter(final PersistenceMapper mapper) {
        this.mapper = mapper
    }

    @Override
    StudentId save(final StudentId id) {
        students.put(id.value, mapper.domainToEntity(id))
        return id
    }

    @Override
    StudentId delete(final StudentId id) {
        students.remove(id.value)
        return id
    }

    @Override
    Collection<StudentId> fetchLazy() {
        return null
    }

    @Override
    Boolean exists(final StudentId id) {
        return students.containsKey(id.value)
    }

    void clear() {
        students.clear()
    }
}
