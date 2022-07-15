package io.metadata.students.adapters.output

import io.github.joke.spockmockable.Mockable
import io.metadata.students.adapters.output.persistence.CoursePersistenceAdapter
import io.metadata.students.adapters.output.persistence.entity.CourseEntity
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.students.adapters.output.persistence.repository.CourseRepository
import io.metadata.students.domain.model.Course
import io.metadata.students.domain.model.CourseName
import spock.lang.Specification

@Mockable([
        CourseName,
        CourseEntity,
        Course
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
}
