package io.metadata.subscriptions.domain.services


import io.metadata.subscriptions.adapter.config.AdapterTestConfiguration
import io.metadata.subscriptions.adapter.output.InMemorySubscriptionPersistenceAdapter
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase
import io.metadata.subscriptions.domain.services.exception.SubscriptionNotAllowedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@Import([AdapterTestConfiguration])
@ActiveProfiles(["kafka-less", "test"])
class SubscriptionServiceIntegrationTest extends Specification {
    @Autowired
    SubscriptionService subscriptionsService

    @Autowired
    InMemorySubscriptionPersistenceAdapter subscriptionAdapter;

    void cleanup() {
        subscriptionAdapter.clear()
    }

    def "1 student subscribe to 1 course"() {
        given:
        def studentId = 1
        def courseId = 1
        createSubscription(courseId, studentId)
        when:
        def responseByCourseId = subscriptionsService.fetchByCourseId(1)
        def responseByStudentId = subscriptionsService.fetchByStudentId(1)
        then:
        responseByCourseId == responseByStudentId
    }

    def "2 students subscribe to 1 course"() {
        given:
        def student1Id = 1
        def student2Id = 2
        def courseId = 1
        createSubscription(courseId, student1Id)
        createSubscription(courseId, student2Id)
        when:
        def responseByCourseId = subscriptionsService.fetchByCourseId(1)
        def responseByStudent1Id = subscriptionsService.fetchByStudentId(1)
        def responseByStudent2Id = subscriptionsService.fetchByStudentId(2)

        then:
        responseByCourseId.courses[0].students.toSet() ==
                (responseByStudent1Id.courses[0].students.toSet() + responseByStudent2Id.courses[0].students.toSet())
        responseByCourseId.courses[0].id == responseByStudent1Id.courses[0].id
        responseByCourseId.courses[0].name == responseByStudent1Id.courses[0].name
        responseByCourseId.courses[0].id == responseByStudent2Id.courses[0].id
        responseByCourseId.courses[0].name == responseByStudent2Id.courses[0].name

    }

    def "1 students subscribe to 2 courses"() {
        given:
        def studentId = 1
        def course1Id = 1
        def course2Id = 2

        createSubscription(course1Id, studentId)
        createSubscription(course2Id, studentId)
        when:
        def responseByCourse1Id = subscriptionsService.fetchByCourseId(1)
        def responseByCourse2Id = subscriptionsService.fetchByCourseId(2)

        def responseByStudentId = subscriptionsService.fetchByStudentId(1)

        then:
        responseByCourse1Id.courses[0].students.toSet() == responseByCourse2Id.courses[0].students.toSet()
        responseByCourse1Id.courses[0].students.toSet() == responseByStudentId.courses[0].students.toSet()
        responseByStudentId.courses.toSet() == (responseByCourse1Id.courses.toSet() + responseByCourse2Id.courses.toSet())
    }

    def "more students than they should subscribe to 1 courses"() {
        when:
        def courseId = 1

        (0..50).each {
            createSubscription(courseId, it)
        }

        then:
        thrown SubscriptionNotAllowedException
    }

    def "1 student subscribe to more courses than it should"() {
        when:
        def studentId = 1

        (0..5).each {
            createSubscription(it, studentId)
        }

        then:
        thrown SubscriptionNotAllowedException
    }

    def createSubscription(Long courseId, Long studentId) {
        def command = SubscribeToCourseUseCase.Command.builder()
                .courseId(courseId)
                .studentId(studentId)
                .build()
        return subscriptionsService.subscribe(command)
    }

}
