package io.metadata.students.adapters.output

import io.github.joke.spockmockable.Mockable
import io.metadata.api.Students
import io.metadata.students.adapters.output.messagesender.StudentMessageSenderAdapter
import org.springframework.kafka.core.KafkaTemplate
import spock.lang.Specification

@Mockable([
        Students.StudentMessage
])
class StudentMessageSenderAdapterTest extends Specification {
    def kafkaTemplate = Mock(KafkaTemplate<String, byte[]>)
    def studentMessageSenderAdapter = new StudentMessageSenderAdapter(kafkaTemplate)

    def "should publish message for created"() {
        given:
        def studentMessage = Mock(Students.StudentMessage)
        byte[] byteArray = []
        when:
        studentMessageSenderAdapter.publishMessageForCreated(studentMessage)
        then:
        1 * studentMessage.toByteArray() >> byteArray
        1 * kafkaTemplate.send("student-created", byteArray) >> {}
    }

    def "should publish message for deleted"() {
        given:
        def studentMessage = Mock(Students.StudentMessage)
        byte[] byteArray = []
        when:
        studentMessageSenderAdapter.publishMessageForDeleted(studentMessage)
        then:
        1 * studentMessage.toByteArray() >> byteArray
        1 * kafkaTemplate.send("student-deleted", byteArray) >> {}
    }

}
