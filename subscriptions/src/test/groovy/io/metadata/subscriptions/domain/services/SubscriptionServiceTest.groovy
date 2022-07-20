package io.metadata.subscriptions.domain.services

import io.github.joke.spockmockable.Mockable
import io.metadata.api.courses.CourseResponse
import io.metadata.api.students.StudentResponse
import io.metadata.api.subscriptions.SubscriptionResponse
import io.metadata.api.subscriptions.SubscriptionsResponse
import io.metadata.subscriptions.domain.model.CourseId
import io.metadata.subscriptions.domain.model.StudentId
import io.metadata.subscriptions.domain.model.Subscription
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort
import io.metadata.subscriptions.domain.ports.output.CourseServicePort
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort
import io.metadata.subscriptions.domain.ports.output.StudentServicePort
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort
import io.metadata.subscriptions.domain.services.exception.CourseNotFoundException
import io.metadata.subscriptions.domain.services.exception.SubscriptionNotAllowedException
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@Mockable([
        Subscription,
        CourseId,
        StudentId,
        SubscriptionResponse,
        SubscriptionsResponse,
        SubscribeToCourseUseCase.Command,
        io.metadata.api.subscriptions.CourseResponse
])
@ActiveProfiles(["kafka-less", "test"])
class SubscriptionServiceTest extends Specification {
    def subscriptionOutputPort = Mock(SubscriptionOutputPort)
    def courseOutputPort = Mock(CourseOutputPort)
    def studentOutputPort = Mock(StudentOutputPort)

    def studentServicePort = Mock(StudentServicePort)

    def courseServicePort = Mock(CourseServicePort)

    def maxCoursesPerStudent = 5L
    def maxStudentsPerCourse = 50L

    def mapper = Mock(ServiceMapper)
    def subscriptionService = Spy(new SubscriptionService(
            subscriptionOutputPort,
            courseOutputPort,
            studentOutputPort,
            studentServicePort,
            courseServicePort,
            maxCoursesPerStudent,
            maxStudentsPerCourse,
            mapper))

    def "should create student"() {
        given:
        def subscription = Mock(Subscription)
        def savedSubscription = Mock(Subscription)
        def courseId = Mock(CourseId)
        def studentId = Mock(StudentId)
        def expectedResponse = Mock(SubscriptionResponse)
        def command = Mock(SubscribeToCourseUseCase.Command)
        when:
        def response = subscriptionService.subscribe(command)
        then:
        1 * mapper.commandToDomain(command) >> subscription
        1 * subscription.getCourseId() >> courseId
        1 * subscription.getStudentId() >> studentId
        1 * courseOutputPort.exists(courseId) >> true
        1 * studentOutputPort.exists(studentId) >> true

        1 * subscriptionOutputPort.countByStudentId(studentId) >> 0L
        1 * subscriptionOutputPort.countByCourseId(courseId) >> 0L

        1 * subscriptionOutputPort.save(subscription) >> savedSubscription
        1 * mapper.domainToResponse(savedSubscription) >> expectedResponse
        response == expectedResponse
    }

    def "should failed when courses count is greater than expected student"() {
        given:
        def subscription = Mock(Subscription)
        def courseId = Mock(CourseId)
        def studentId = Mock(StudentId)
        def command = Mock(SubscribeToCourseUseCase.Command)
        when:
        subscriptionService.subscribe(command)
        then:
        1 * mapper.commandToDomain(command) >> subscription
        1 * subscription.getCourseId() >> courseId
        1 * subscription.getStudentId() >> studentId
        1 * courseOutputPort.exists(courseId) >> true
        1 * studentOutputPort.exists(studentId) >> true
        1 * subscriptionOutputPort.countByStudentId(studentId) >> 0L
        1 * subscriptionOutputPort.countByCourseId(courseId) >> maxStudentsPerCourse
        thrown SubscriptionNotAllowedException
    }

    def "should failed when students count is greater than expected student"() {
        given:
        def subscription = Mock(Subscription)
        def courseId = Mock(CourseId)
        def studentId = Mock(StudentId)
        def command = Mock(SubscribeToCourseUseCase.Command)
        when:
        subscriptionService.subscribe(command)
        then:
        1 * mapper.commandToDomain(command) >> subscription
        1 * subscription.getStudentId() >> studentId
        1 * subscription.getCourseId() >> courseId

        1 * courseOutputPort.exists(courseId) >> true
        1 * studentOutputPort.exists(studentId) >> true
        1 * subscriptionOutputPort.countByStudentId(studentId) >> maxCoursesPerStudent
        thrown SubscriptionNotAllowedException
    }


    def "should fetch by student id"() {
        given:
        def courseId = Mock(CourseId)
        def studentId = Mock(StudentId)

        def courseResponse = Mock(CourseResponse)

        def subscription = Mock(Subscription)
        when:
        def studentResponses = subscriptionService.fetchByStudentId(studentId.value)
        then:
        1 * mapper.mapStudentId(studentId.value) >> studentId
        1 * subscriptionOutputPort.fetchByStudentId(studentId) >> [subscription]
        1 * subscription.getCourseId() >> courseId
        1 * courseServicePort.getById(courseId) >> courseResponse

        studentResponses == [courseResponse]
    }


    def "should fetch by course id"() {
        given:
        def courseId = Mock(CourseId)
        def studentId = Mock(StudentId)

        def studentResponse = Mock(StudentResponse)

        def subscription = Mock(Subscription)
        when:
        def studentResponses = subscriptionService.fetchByCourseId(courseId.value)
        then:
        1 * mapper.mapCourseId(courseId.value) >> courseId
        1 * subscriptionOutputPort.fetchByCourseId(courseId) >> [subscription]
        1 * subscription.getStudentId() >> studentId
        1 * studentServicePort.getById(studentId) >> studentResponse

        studentResponses == [studentResponse]
    }
}
