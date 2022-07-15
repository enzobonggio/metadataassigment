package io.metadata.students.adapters.output

import io.github.joke.spockmockable.Mockable
import io.metadata.api.Courses
import io.metadata.students.adapters.output.messagesender.CourseMessageSenderAdapter
import org.springframework.kafka.core.KafkaTemplate
import spock.lang.Specification

@Mockable([
        Courses.CourseMessage
])
class CourseMessageSenderAdapterTest extends Specification {
    def kafkaTemplate = Mock(KafkaTemplate<String, byte[]>)
    def courseMessageSenderAdapter = new CourseMessageSenderAdapter(kafkaTemplate)

    def "should publish message for created"() {
        given:
        def courseMessage = Mock(Courses.CourseMessage)
        byte[] byteArray = []
        when:
        courseMessageSenderAdapter.publishMessageForCreated(courseMessage)
        then:
        1 * courseMessage.toByteArray() >> byteArray
        1 * kafkaTemplate.send("course-created", byteArray) >> {}
    }

    def "should publish message for deleted"() {
        given:
        def courseMessage = Mock(Courses.CourseMessage)
        byte[] byteArray = []
        when:
        courseMessageSenderAdapter.publishMessageForDeleted(courseMessage)
        then:
        1 * courseMessage.toByteArray() >> byteArray
        1 * kafkaTemplate.send("course-deleted", byteArray) >> {}
    }

}
