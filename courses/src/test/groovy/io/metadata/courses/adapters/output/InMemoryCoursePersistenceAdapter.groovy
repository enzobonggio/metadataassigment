package io.metadata.courses.adapters.output

import io.metadata.api.commons.State
import io.metadata.courses.adapters.output.persistence.entity.CourseEntity
import io.metadata.courses.adapters.output.persistence.exception.CourseNotFoundException
import io.metadata.courses.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.courses.domain.model.Course
import io.metadata.courses.domain.model.CourseId
import io.metadata.courses.domain.model.CourseName
import io.metadata.courses.domain.ports.output.CourseOutputPort

class InMemoryCoursePersistenceAdapter implements CourseOutputPort {

    private final PersistenceMapper mapper
    private final List<CourseEntity> courses = []

    InMemoryCoursePersistenceAdapter(final PersistenceMapper mapper) {
        this.mapper = mapper
    }

    @Override
    Course save(final CourseName name) {
        def id = courses.size()
        def entity = mapper.domainToEntity(name).withId(id)
        courses.add(entity)
        return mapper.entityToDomain(entity)
    }

    @Override
    void update(final Course course) {
        def entity = mapper.domainToEntity(course)
        def idToUpdate = (int) entity.id
        if (courses.size() >= idToUpdate) {
            def updatedCourse = courses[idToUpdate].withName(course.name.value)
            courses.remove(idToUpdate)
            courses.add(idToUpdate, updatedCourse)
        }
    }

    @Override
    void updateState(final CourseId id, final State state) {
        def idToUpdate = (int) id.value
        if (courses.size() >= idToUpdate) {
            def updatedCourse = courses[idToUpdate].withState(state.toString())
            courses.remove(idToUpdate)
            courses.add(idToUpdate, updatedCourse)
        }
    }

    @Override
    Course getById(final CourseId id) {
        def idValue = (int) id.value
        if (courses.size() > idValue) {
            return mapper.entityToDomain(courses[idValue])
        }
        throw new CourseNotFoundException(id.value)
    }

    @Override
    void deleteById(final CourseId id) {
        def idToDelete = (int) id.value
        if (courses.size() >= idToDelete) {
            courses.remove(idToDelete)
        }
    }

    @Override
    Collection<Course> fetch() {
        return courses.collect { mapper.entityToDomain(it) }
    }

    void clear() {
        courses.clear()
    }
}
