package io.metadata.subscriptions.domain.services

import io.metadata.subscriptions.adapter.config.AdapterTestConfiguration
import io.metadata.subscriptions.adapter.output.InMemoryCoursePersistenceAdapter
import io.metadata.subscriptions.adapter.output.InMemoryStudentPersistenceAdapter
import io.metadata.subscriptions.adapter.output.InMemorySubscriptionPersistenceAdapter
import io.metadata.subscriptions.adapters.output.persistence.entity.CourseEntity
import io.metadata.subscriptions.adapters.output.persistence.repository.StudentRepository
import io.metadata.subscriptions.domain.model.CourseId
import io.metadata.subscriptions.domain.model.StudentId
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
    InMemorySubscriptionPersistenceAdapter subscriptionPersistenceAdapter

    @Autowired
    InMemoryCoursePersistenceAdapter coursePersistenceAdapter

    @Autowired
    InMemoryStudentPersistenceAdapter studentPersistenceAdapter

    void cleanup() {
        subscriptionPersistenceAdapter.clear()
        coursePersistenceAdapter.clear()
        studentPersistenceAdapter.clear()
    }

    def "1 student subscribe to 1 course"() {
        given:
        def studentId = 1L
        def courseId = 1L
        createCourse(courseId)
        createStudent(studentId)
        createSubscription(courseId, studentId)
        when:
        def responseByCourseId = subscriptionsService.fetchByCourseId(1)
        def responseByStudentId = subscriptionsService.fetchByStudentId(1)
        then:
        responseByCourseId.collect { it.id }.toSet() == [studentId].toSet()
        responseByStudentId.collect { it.id }.toSet() == [courseId].toSet()
    }

    def "2 students subscribe to 1 course"() {
        given:
        def student1Id = 1L
        def student2Id = 2L
        def courseId = 1L
        createCourse(courseId)
        createStudent(student1Id)
        createStudent(student2Id)

        createSubscription(courseId, student1Id)
        createSubscription(courseId, student2Id)
        when:
        def responseByCourseId = subscriptionsService.fetchByCourseId(1)
        def responseByStudent1Id = subscriptionsService.fetchByStudentId(1)
        def responseByStudent2Id = subscriptionsService.fetchByStudentId(2)

        then:
        responseByCourseId.collect { it.id }.toSet() == [student1Id, student2Id].toSet()
        responseByStudent1Id.collect { it.id }.toSet() == [courseId].toSet()
        responseByStudent2Id.collect { it.id }.toSet() == [courseId].toSet()
    }

    def "1 students subscribe to 2 courses"() {
        given:
        def studentId = 1L
        def course1Id = 1L
        def course2Id = 2L
        createCourse(course1Id)
        createCourse(course2Id)
        createStudent(studentId)

        createSubscription(course1Id, studentId)
        createSubscription(course2Id, studentId)
        when:
        def responseByCourse1Id = subscriptionsService.fetchByCourseId(1)
        def responseByCourse2Id = subscriptionsService.fetchByCourseId(2)

        def responseByStudentId = subscriptionsService.fetchByStudentId(1)

        then:
        responseByCourse1Id.collect { it.id }.toSet() == [studentId].toSet()
        responseByCourse2Id.collect { it.id }.toSet() == [studentId].toSet()
        responseByStudentId.collect { it.id }.toSet() == [course1Id, course2Id].toSet()
    }

    def "more students than they should subscribe to 1 courses"() {
        when:
        def courseId = 1
        createCourse(courseId)
        (0..50).each {
            createStudent(it)
            createSubscription(courseId, it)
        }

        then:
        thrown SubscriptionNotAllowedException
    }

    def "1 student subscribe to more courses than it should"() {
        when:
        def studentId = 1
        createStudent(studentId)

        (0..5).each {
            createCourse(it)
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

    def createCourse(long courseId) {
        coursePersistenceAdapter.save(CourseId.of(courseId))
    }

    def createStudent(long studentId) {
        studentPersistenceAdapter.save(StudentId.of(studentId))
    }

}
