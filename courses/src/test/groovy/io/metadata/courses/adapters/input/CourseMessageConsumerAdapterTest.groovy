package io.metadata.courses.adapters.input

import io.github.joke.spockmockable.Mockable
import io.metadata.api.Subscriptions
import io.metadata.api.courses.CourseResponse
import io.metadata.courses.adapters.input.messageconsumer.CourseMessageConsumerAdapter
import io.metadata.courses.adapters.input.messageconsumer.mapper.MessageConsumerMapper
import io.metadata.courses.domain.ports.input.UpdateCourseStateUseCase
import spock.lang.Specification

@Mockable([
        UpdateCourseStateUseCase.Command,
        CourseResponse
])
class CourseMessageConsumerAdapterTest extends Specification {
    def updateCourseStateUseCase = Mock(UpdateCourseStateUseCase)
    def mapper = Mock(MessageConsumerMapper)
    def courseMessageConsumerAdapter = new CourseMessageConsumerAdapter(updateCourseStateUseCase, mapper)

    def "should consume message for subscription-course created"() {
        def message = Subscriptions.CourseMessage.newBuilder().build()
        byte[] bytes = message.toByteArray()
        def command = Mock(UpdateCourseStateUseCase.Command)
        def courseResponse = Mock(CourseResponse)
        when:
        courseMessageConsumerAdapter.consumeMessageForSubscriptionCourseCreated(bytes)
        then:
        1 * mapper.messageToCommand(message) >> command
        1 * updateCourseStateUseCase.updateState(command) >> courseResponse
    }
}
