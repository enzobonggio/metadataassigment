package io.metadata.students.adapters.input.messageconsumer;

import com.google.protobuf.InvalidProtocolBufferException;
import io.metadata.api.Subscriptions;
import io.metadata.students.adapters.mapper.AdapterMapper;
import io.metadata.students.domain.ports.input.CourseMessageConsumer;
import io.metadata.students.domain.ports.input.UpdateCourseStateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@Slf4j
public class CourseMessageConsumerAdapter implements CourseMessageConsumer
{
    private static final String EXEC = "SubscriptionMessageConsumerAdapter";

    private final UpdateCourseStateUseCase updateCourseStateUseCase;

    private final AdapterMapper restMapper;

    @Override
    @KafkaListener(id = EXEC + "_subscription-course", topics = "subscription-course-created")
    public void consumeMessageForSubscriptionCourseCreated(final byte[] event)
    {
        Subscriptions.CourseMessage message = null;
        try {
            message = Subscriptions.CourseMessage.parseFrom(event);
        }
        catch (InvalidProtocolBufferException e) {
            log.warn("Malformed message {}", event, e);
        }
        try {
            val command = restMapper.messageToCommand(message);
            updateCourseStateUseCase.updateState(command);
        }
        catch (Exception e) {
            log.error("Unexpected Error while consuming message {}", message, e);
        }
    }
}
