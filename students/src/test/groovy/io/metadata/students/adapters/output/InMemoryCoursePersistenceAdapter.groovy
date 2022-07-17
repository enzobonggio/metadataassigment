package io.metadata.students.adapters.output

import io.metadata.api.commons.State
import io.metadata.students.adapters.output.persistence.entity.StudentEntity
import io.metadata.students.adapters.output.persistence.exception.StudentNotFoundException
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.students.domain.model.Student
import io.metadata.students.domain.model.StudentId
import io.metadata.students.domain.model.StudentName
import io.metadata.students.domain.ports.output.StudentOutputPort

class InMemoryStudentPersistenceAdapter implements StudentOutputPort {

    private final PersistenceMapper mapper
    private final List<StudentEntity> students = []

    InMemoryStudentPersistenceAdapter(final PersistenceMapper mapper) {
        this.mapper = mapper
    }

    @Override
    Student save(final StudentName name) {
        def id = students.size()
        def entity = mapper.domainToEntity(name).withId(id)
        students.add(entity)
        return mapper.entityToDomain(entity)
    }

    @Override
    void update(final Student student) {
        def entity = mapper.domainToEntity(student)
        def idToUpdate = (int) entity.id
        if (students.size() >= idToUpdate) {
            def updatedStudent = students[idToUpdate].withName(student.name.value)
            students.remove(idToUpdate)
            students.add(idToUpdate, updatedStudent)
        }
    }

    @Override
    void updateState(final StudentId id, final State state) {
        def idToUpdate = (int) id.value
        if (students.size() >= idToUpdate) {
            def updatedStudent = students[idToUpdate].withState(state.toString())
            students.remove(idToUpdate)
            students.add(idToUpdate, updatedStudent)
        }
    }

    @Override
    Student getById(final StudentId id) {
        def idValue = (int) id.value
        if (students.size() > idValue) {
            return mapper.entityToDomain(students[idValue])
        }
        throw new StudentNotFoundException(id.value)
    }

    @Override
    void deleteById(final StudentId id) {
        def idToDelete = (int) id.value
        if (students.size() >= idToDelete) {
            students.remove(idToDelete)
        }
    }

    @Override
    Collection<Student> fetch() {
        return students.collect { mapper.entityToDomain(it) }
    }

    void clear() {
        students.clear()
    }
}
