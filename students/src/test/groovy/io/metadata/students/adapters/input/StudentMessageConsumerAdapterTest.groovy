package io.metadata.students.adapters.input

import io.github.joke.spockmockable.Mockable
import io.metadata.api.Subscriptions
import io.metadata.api.students.StudentResponse
import io.metadata.students.adapters.input.messageconsumer.StudentMessageConsumerAdapter
import io.metadata.students.adapters.input.messageconsumer.mapper.MessageConsumerMapper
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase
import spock.lang.Specification

@Mockable([
        UpdateStudentStateUseCase.Command,
        StudentResponse
])
class StudentMessageConsumerAdapterTest extends Specification {
    def updateStudentStateUseCase = Mock(UpdateStudentStateUseCase)
    def mapper = Mock(MessageConsumerMapper)
    def studentMessageConsumerAdapter = new StudentMessageConsumerAdapter(updateStudentStateUseCase, mapper)

    def "should consume message for subscription-student created"() {
        def message = Subscriptions.StudentMessage.newBuilder().build()
        byte[] bytes = message.toByteArray()
        def command = Mock(UpdateStudentStateUseCase.Command)
        def studentResponse = Mock(StudentResponse)
        when:
        studentMessageConsumerAdapter.consumeMessageForSubscriptionStudentCreated(bytes)
        then:
        1 * mapper.messageToCommand(message) >> command
        1 * updateStudentStateUseCase.updateState(command) >> studentResponse
    }
}
