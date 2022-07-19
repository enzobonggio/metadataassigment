package io.metadata.subscriptions.adapter.input

import io.metadata.api.Courses
import io.metadata.api.Students
import io.metadata.subscriptions.adapter.config.AdapterTestConfiguration
import io.metadata.subscriptions.adapters.input.messageconsumer.SubscriptionMessageConsumerAdapter
import io.metadata.subscriptions.adapters.output.persistence.entity.CourseEntity
import io.metadata.subscriptions.adapters.output.persistence.entity.StudentEntity
import io.metadata.subscriptions.adapters.output.persistence.repository.CourseRepository
import io.metadata.subscriptions.adapters.output.persistence.repository.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Timeout
import spock.util.concurrent.PollingConditions

import java.sql.Timestamp
import java.time.Instant
import java.util.concurrent.TimeUnit

@SpringBootTest
@Import([AdapterTestConfiguration])
@EmbeddedKafka(
        partitions = 1,
        controlledShutdown = false,
        brokerProperties = [
                "listeners=PLAINTEXT://localhost:3333",
                "port=3333"
        ],
        topics = [
                "student-created",
                "course-created",
                "course-deleted",
                "student-deleted"
        ])
@ActiveProfiles("test")
class SubscriptionMessageConsumerAdapterIntegrationTest extends Specification {

    @Autowired
    SubscriptionMessageConsumerAdapter subscriptionMessageConsumerAdapter

    @Autowired
    KafkaTemplate<String, byte[]> template

    @Autowired
    StudentRepository studentRepository

    @Autowired
    CourseRepository courseRepository

    def conditions = new PollingConditions(timeout: 10, initialDelay: 1.5, factor: 1.25)
    def random = new Random()

    @Timeout(value = 10_000, unit = TimeUnit.MILLISECONDS)
    def "should consume 'student-created' message "() {
        given:
        def id = random.nextLong()
        when:
        template.send("student-created", Students.StudentMessage.newBuilder()
                .setId(id)
                .build().toByteArray())
        then:
        conditions.eventually {
            studentRepository.findById(id).isPresent()
        }
    }

    @Timeout(value = 10_000, unit = TimeUnit.MILLISECONDS)
    def "should consume 'course-created' message "() {
        given:
        def id = random.nextLong()
        when:
        template.send("course-created", Courses.CourseMessage.newBuilder()
                .setId(id)
                .build().toByteArray())
        then:
        conditions.eventually {
            courseRepository.findById(id).isPresent()
        }
    }

    @Timeout(value = 10_000, unit = TimeUnit.MILLISECONDS)
    def "should consume 'student-deleted' message "() {
        given:
        def id = random.nextLong()
        studentRepository.save(StudentEntity.builder()
                .id(id)
                .createdAt(Timestamp.from(Instant.now()))
                .build())
        when:
        template.send("student-deleted", Students.StudentMessage.newBuilder()
                .setId(id)
                .build().toByteArray())
        then:
        conditions.eventually {
            !studentRepository.findById(id).isPresent()
        }
    }

    @Timeout(value = 10_000, unit = TimeUnit.MILLISECONDS)
    def "should consume 'course-deleted' message "() {
        given:
        def id = random.nextLong()
        courseRepository.save(CourseEntity.builder()
                .id(id)
                .createdAt(Timestamp.from(Instant.now()))
                .build())
        when:
        template.send("course-deleted", Courses.CourseMessage.newBuilder()
                .setId(id)
                .build().toByteArray())
        then:
        conditions.eventually {
            !courseRepository.findById(id).isPresent()
        }
    }
}
