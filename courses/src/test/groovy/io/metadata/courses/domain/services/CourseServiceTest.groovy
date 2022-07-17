package io.metadata.courses.domain.services

import io.github.joke.spockmockable.Mockable
import io.metadata.api.Courses
import io.metadata.api.commons.State
import io.metadata.api.courses.CourseResponse
import io.metadata.courses.domain.model.Course
import io.metadata.courses.domain.model.CourseId
import io.metadata.courses.domain.model.CourseName
import io.metadata.courses.domain.ports.input.CreateCourseUseCase
import io.metadata.courses.domain.ports.input.DeleteCourseUseCase
import io.metadata.courses.domain.ports.input.UpdateCourseStateUseCase
import io.metadata.courses.domain.ports.input.UpdateCourseUseCase
import io.metadata.courses.domain.ports.output.CourseMessageSender
import io.metadata.courses.domain.ports.output.CourseOutputPort
import io.metadata.courses.domain.services.mapper.ServiceMapper
import spock.lang.Specification

@Mockable([
        CourseResponse,
        CourseName,
        CourseId,
        Course,
        DeleteCourseUseCase.Command,
        UpdateCourseUseCase.Command,
        CreateCourseUseCase.Command,
        UpdateCourseStateUseCase.Command,
        Courses.CourseMessage])
class CourseServiceTest extends Specification {
    def courseOutputPort = Mock(CourseOutputPort)
    def courseEventPublisher = Mock(CourseMessageSender)
    def mapper = Mock(ServiceMapper)
    def courseService = Spy(new CourseService(courseOutputPort, courseEventPublisher, mapper))

    def "should create course"() {
        given:
        def courseName = Mock(CourseName)
        def courseId = Mock(CourseId)
        def course = Mock(Course)
        def courseMessage = Mock(Courses.CourseMessage)
        def command = Mock(CreateCourseUseCase.Command)
        def expectedCourseIdValue = 10L
        when:
        def courseIdValue = courseService.create(command)
        then:
        1 * mapper.commandToCourseName(command) >> courseName
        1 * courseOutputPort.save(courseName) >> course
        1 * mapper.courseToMessage(course) >> courseMessage
        1 * courseEventPublisher.publishMessageForCreated(courseMessage) >> {}
        1 * course.getId() >> courseId
        1 * courseId.getValue() >> expectedCourseIdValue
        courseIdValue == expectedCourseIdValue
    }

    def "should update course"() {
        given:
        def courseToUpdate = Mock(Course)
        def command = Mock(UpdateCourseUseCase.Command)
        def id = 10L
        def expectedCourseResponse = Mock(CourseResponse)
        when:
        def courseResponse = courseService.update(command)
        then:
        1 * mapper.commandToDomain(command) >> courseToUpdate
        1 * courseOutputPort.update(courseToUpdate) >> {}
        1 * command.getId() >> id
        1 * courseService.getById(id) >> expectedCourseResponse
        courseResponse == expectedCourseResponse
    }

    def "should delete course"() {
        given:
        def courseId = Mock(CourseId)
        def command = Mock(DeleteCourseUseCase.Command)
        def courseMessage = Mock(Courses.CourseMessage)
        when:
        courseService.delete(command)
        then:
        1 * mapper.commandToCourseId(command) >> courseId
        1 * mapper.courseIdToMessage(courseId) >> courseMessage
        1 * courseOutputPort.deleteById(courseId) >> {}
        1 * courseEventPublisher.publishMessageForDeleted(courseMessage) >> {}
    }

    def "should get course by id"() {
        given:
        def courseIdValue = 10L
        def courseId = Mock(CourseId)
        def course = Mock(Course)
        def expectedCourseResponse = Mock(CourseResponse)
        when:
        def courseResponse = courseService.getById(courseIdValue)
        then:
        1 * mapper.mapId(courseIdValue) >> courseId
        1 * courseOutputPort.getById(courseId) >> course
        1 * mapper.domainToResponse(course) >> expectedCourseResponse
        courseResponse == expectedCourseResponse
    }

    def "should fetch courses"() {
        given:
        def course = Mock(Course)
        def expectedCourseResponse = Mock(CourseResponse)
        when:
        def courseResponses = courseService.fetch()
        then:
        1 * courseOutputPort.fetch() >> [course]
        1 * mapper.domainToResponse(course) >> expectedCourseResponse
        courseResponses == [expectedCourseResponse]
    }


    def "should update state"() {
        given:
        def command = Mock(UpdateCourseStateUseCase.Command)
        def courseId = Mock(CourseId)
        def courseIdValue = 10L
        def expectedCourseResponse = Mock(CourseResponse)
        when:
        def courseResponse = courseService.updateState(command)
        then:
        1 * command.getId() >> courseIdValue
        1 * command.getState() >> State.CREATING
        1 * mapper.mapId(courseIdValue) >> courseId
        1 * courseOutputPort.updateState(courseId, State.CREATING) >> {}
        1 * courseService.getById(courseIdValue) >> expectedCourseResponse
        courseResponse == expectedCourseResponse
    }
}
