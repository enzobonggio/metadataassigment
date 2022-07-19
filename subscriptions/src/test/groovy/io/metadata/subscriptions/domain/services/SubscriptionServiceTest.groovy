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
import io.metadata.subscriptions.domain.ports.output.CourseServicePort
import io.metadata.subscriptions.domain.ports.output.StudentServicePort
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort
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
    def studentServicePort = Mock(StudentServicePort)

    def courseServicePort = Mock(CourseServicePort)

    def maxCoursesPerStudent = 5L
    def maxStudentsPerCourse = 50L

    def mapper = Mock(ServiceMapper)
    def subscriptionService = Spy(new SubscriptionService(
            subscriptionOutputPort,
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
        1 * subscriptionOutputPort.countByStudentId(studentId) >> 0L
        1 * subscriptionOutputPort.countByCourseId(courseId) >> maxStudentsPerCourse
        thrown SubscriptionNotAllowedException
    }

    def "should failed when students count is greater than expected student"() {
        given:
        def subscription = Mock(Subscription)
        def studentId = Mock(StudentId)
        def command = Mock(SubscribeToCourseUseCase.Command)
        when:
        subscriptionService.subscribe(command)
        then:
        1 * mapper.commandToDomain(command) >> subscription
        1 * subscription.getStudentId() >> studentId
        1 * subscriptionOutputPort.countByStudentId(studentId) >> maxCoursesPerStudent
        thrown SubscriptionNotAllowedException
    }


    def "should fetch by student id"() {
        given:
        def courseId = CourseId.of(10L)
        def studentId = StudentId.of(10L)

        def studentResponse = Mock(StudentResponse)
        def courseResponse = Mock(CourseResponse)

        def subscription = Mock(Subscription)
        def courseSubscriptionResponse = Mock(io.metadata.api.subscriptions.CourseResponse)
        def expectedResponse = Mock(SubscriptionsResponse)
        when:
        def studentResponses = subscriptionService.fetchByStudentId(studentId.value)
        then:
        1 * mapper.mapStudentId(studentId.value) >> studentId
        1 * subscriptionOutputPort.fetchByStudentId(studentId) >> [subscription]
        2 * subscription.getStudentId() >> studentId
        2 * subscription.getCourseId() >> courseId
        1 * studentResponse.getId() >> studentId.value
        1 * courseResponse.getId() >> courseId.value
        1 * studentServicePort.getById(studentId) >> studentResponse
        1 * courseServicePort.getById(courseId) >> courseResponse

        1 * mapper.entryToCourseResponse(courseResponse, [studentResponse]) >> courseSubscriptionResponse
        1 * mapper.coursesResponsesToSubscriptionsResponse([courseSubscriptionResponse]) >> expectedResponse
        studentResponses == expectedResponse
    }


    def "should fetch by course id"() {
        given:
        def courseId = CourseId.of(10L)
        def studentId = StudentId.of(10L)

        def studentResponse = Mock(StudentResponse)
        def courseResponse = Mock(CourseResponse)

        def subscription = Mock(Subscription)
        def courseSubscriptionResponse = Mock(io.metadata.api.subscriptions.CourseResponse)
        def expectedResponse = Mock(SubscriptionsResponse)
        when:
        def studentResponses = subscriptionService.fetchByCourseId(courseId.value)
        then:
        1 * mapper.mapCourseId(courseId.value) >> courseId
        1 * subscriptionOutputPort.fetchByCourseId(courseId) >> [subscription]
        2 * subscription.getStudentId() >> studentId
        2 * subscription.getCourseId() >> courseId
        1 * studentResponse.getId() >> studentId.value
        1 * courseResponse.getId() >> courseId.value
        1 * studentServicePort.getById(studentId) >> studentResponse
        1 * courseServicePort.getById(courseId) >> courseResponse

        1 * mapper.entryToCourseResponse(courseResponse, [studentResponse]) >> courseSubscriptionResponse
        1 * mapper.coursesResponsesToSubscriptionsResponse([courseSubscriptionResponse]) >> expectedResponse
        studentResponses == expectedResponse
    }
}
