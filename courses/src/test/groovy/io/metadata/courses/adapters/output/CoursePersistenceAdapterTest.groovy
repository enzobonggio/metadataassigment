package io.metadata.courses.adapters.output

import io.github.joke.spockmockable.Mockable
import io.metadata.api.commons.State
import io.metadata.courses.adapters.output.persistence.CoursePersistenceAdapter
import io.metadata.courses.adapters.output.persistence.entity.CourseEntity
import io.metadata.courses.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.courses.adapters.output.persistence.repository.CourseRepository
import io.metadata.courses.domain.model.Course
import io.metadata.courses.domain.model.CourseId
import io.metadata.courses.domain.model.CourseName
import spock.lang.Specification

@Mockable([
        CourseName,
        CourseEntity,
        Course,
        CourseId
])
class CoursePersistenceAdapterTest extends Specification {

    def courseRepository = Mock(CourseRepository)
    def mapper = Mock(PersistenceMapper)
    def coursePersistenceAdapter = new CoursePersistenceAdapter(courseRepository, mapper)

    def "should save course"() {
        given:
        def courseName = Mock(CourseName)
        def courseEntity = Mock(CourseEntity)
        def courseDomain = Mock(Course)
        when:
        def course = coursePersistenceAdapter.save(courseName)
        then:
        1 * mapper.domainToEntity(courseName) >> courseEntity
        1 * mapper.entityToDomain(courseEntity) >> courseDomain
        1 * courseRepository.save(courseEntity) >> courseEntity
        course == courseDomain
    }

    def "should update course"() {
        given:
        def courseEntity = Mock(CourseEntity)
        def courseToUpdate = Mock(Course)
        def courseIdValue = 10L
        def courseNameValue = "name"
        when:
        coursePersistenceAdapter.update(courseToUpdate)
        then:
        1 * mapper.domainToEntity(courseToUpdate) >> courseEntity
        1 * courseEntity.getId() >> courseIdValue
        1 * courseEntity.getName() >> courseNameValue
        1 * courseRepository.update(courseIdValue, courseNameValue) >> courseIdValue
    }

    def "should update state course"() {
        given:
        def courseIdToUpdate = Mock(CourseId)
        def courseStateToUpdate = State.CREATED
        def courseIdValue = 10L
        when:
        coursePersistenceAdapter.updateState(courseIdToUpdate, courseStateToUpdate)
        then:
        1 * courseIdToUpdate.getValue() >> courseIdValue
        1 * courseRepository.updateState(courseIdValue, courseStateToUpdate.toString()) >> courseIdValue
    }

    def "should delete course"() {
        given:
        def courseIdToDelete = Mock(CourseId)
        def courseIdValue = 10L
        when:
        coursePersistenceAdapter.deleteById(courseIdToDelete)
        then:
        1 * courseIdToDelete.getValue() >> courseIdValue
        1 * courseRepository.deleteById(courseIdValue) >> {}
    }

    def "should fetch courses"() {
        given:
        def courseEntity = Mock(CourseEntity)
        def courseEntities = [courseEntity]
        def course = Mock(Course)
        def expectedCourses = [course]
        when:
        def courses = coursePersistenceAdapter.fetch()
        then:
        1 * courseRepository.findAll() >> courseEntities
        1 * mapper.entityToDomain(courseEntity) >> course
        courses == expectedCourses
    }
}
