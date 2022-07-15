package io.metadata.students.adapters.input

import io.github.joke.spockmockable.Mockable
import io.metadata.api.Subscriptions
import io.metadata.students.adapters.input.messageconsumer.CourseMessageConsumerAdapter
import io.metadata.students.adapters.mapper.AdapterMapper
import io.metadata.students.domain.ports.input.UpdateCourseStateUseCase
import spock.lang.Specification

@Mockable({
    UpdateCourseStateUseCase.Command
})
class CourseMessageConsumerAdapterTest extends Specification {
    def updateCourseStateUseCase = Mock(UpdateCourseStateUseCase)
    def mapper = Mock(AdapterMapper)
    def courseMessageConsumerAdapter = new CourseMessageConsumerAdapter(updateCourseStateUseCase, mapper)

    def "should consume message for subscription-course created"() {
        def message = Subscriptions.CourseMessage.newBuilder().build()
        byte[] bytes = message.toByteArray()
        def command = Mock(UpdateCourseStateUseCase.Command)
        def courseId = 10L
        when:
        courseMessageConsumerAdapter.consumeMessageForSubscriptionCourseCreated(bytes)
        then:
        1 * mapper.messageToCommand(message) >> command
        1 * updateCourseStateUseCase.updateState(command) >> courseId
    }
}
