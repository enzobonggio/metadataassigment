package io.metadata.students.adapters.input.messageconsumer;

import com.google.protobuf.InvalidProtocolBufferException;
import io.metadata.api.Subscriptions;
import io.metadata.students.adapters.input.messageconsumer.mapper.MessageConsumerMapper;
import io.metadata.students.domain.ports.input.StudentMessageConsumer;
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@Slf4j
public class StudentMessageConsumerAdapter implements StudentMessageConsumer
{
    private static final String EXEC = "SubscriptionMessageConsumerAdapter";

    private final UpdateStudentStateUseCase updateStudentStateUseCase;

    private final MessageConsumerMapper mapper;

    @Override
    @KafkaListener(id = EXEC + "_subscription-student", topics = "subscription-student-created")
    public void consumeMessageForSubscriptionStudentCreated(final byte[] event)
    {
        Subscriptions.StudentMessage message = null;
        try {
            message = Subscriptions.StudentMessage.parseFrom(event);
        }
        catch (InvalidProtocolBufferException e) {
            log.warn("Malformed message {}", event, e);
        }
        try {
            val command = mapper.messageToCommand(message);
            updateStudentStateUseCase.updateState(command);
        }
        catch (Exception e) {
            log.error("Unexpected Error while consuming message {}", message, e);
        }
    }
}
