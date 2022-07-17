package io.metadata.courses.domain.services

import io.metadata.api.commons.State
import io.metadata.courses.adapters.config.AdapterTestConfiguration
import io.metadata.courses.adapters.output.InMemoryCoursePersistenceAdapter
import io.metadata.courses.adapters.output.persistence.exception.CourseNotFoundException
import io.metadata.courses.domain.ports.input.CreateCourseUseCase
import io.metadata.courses.domain.ports.input.DeleteCourseUseCase
import io.metadata.courses.domain.ports.input.UpdateCourseStateUseCase
import io.metadata.courses.domain.ports.input.UpdateCourseUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@Import([AdapterTestConfiguration])
@ActiveProfiles(["kafka-less", "test"])
class CourseServiceIntegrationTest extends Specification {
    @Autowired
    CourseService courseService

    @Autowired
    InMemoryCoursePersistenceAdapter courseAdapter;

    void cleanup() {
        courseAdapter.clear()
    }

    def "get created course"() {
        given:
        def courseId = createCourse("name")
        when:
        def course = courseService.getById(courseId)
        then:
        courseId == course.id
        course.name == "name"
        course.state == State.CREATING

    }

    def "get updated course"() {
        given:
        def courseId = createCourse("name")
        def command = UpdateCourseUseCase.Command.builder()
                .name("name2")
                .id(courseId)
                .build()
        when:
        courseService.update(command)
        def course = courseService.getById(courseId)
        then:
        courseId == course.id
        course.name == "name2"
        course.state == State.CREATING
    }

    def "get delete course"() {
        given:
        def courseId = createCourse("name")
        def command = DeleteCourseUseCase.Command.builder()
                .id(courseId)
                .build()
        when:
        courseService.delete(command)
        def course = courseService.getById(courseId)
        then:
        thrown CourseNotFoundException
    }

    def "fetch created course"() {
        given:
        def courseId1 = createCourse("name1")
        def courseId2 = createCourse("name2")
        when:
        def courseResponses = courseService.fetch()
        then:
        courseResponses.collect { it.name }.toSet() == ["name1", "name2"].toSet()
        courseResponses.collect { it.id }.toSet() == [courseId1, courseId2].toSet()
    }

    def "get updated state course"() {
        given:
        def courseId = createCourse("name")
        def command = UpdateCourseStateUseCase.Command.builder()
                .id(courseId)
                .state(State.CREATED)
                .build()
        when:
        courseService.updateState(command)
        def course = courseService.getById(courseId)
        then:
        courseId == course.id
        course.name == "name"
        course.state == State.CREATED
    }

    def createCourse(String name) {
        def command = CreateCourseUseCase.Command.builder()
                .name(name)
                .build()
        return courseService.create(command)
    }

}
