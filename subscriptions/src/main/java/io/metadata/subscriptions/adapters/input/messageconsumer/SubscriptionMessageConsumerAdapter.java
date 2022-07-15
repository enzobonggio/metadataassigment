package io.metadata.subscriptions.adapters.input.messageconsumer;

import com.google.protobuf.InvalidProtocolBufferException;
import io.metadata.api.Courses;
import io.metadata.api.Students;
import io.metadata.subscriptions.adapters.input.mapper.RestMapper;
import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscriptionMessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@Slf4j
public class SubscriptionMessageConsumerAdapter implements SubscriptionMessageConsumer
{
    private static final String EXEC = "SubscriptionMessageConsumerAdapter";

    private final CreateStudentUseCase createStudentUseCase;
    private final CreateCourseUseCase createCourseUseCase;

    private final RestMapper restMapper;

    @Override
    @KafkaListener(id = EXEC + "_student", topics = "student-created")
    public void consumeMessageForStudentCreated(final byte[] event)
    {
        Students.StudentMessage message = null;
        try {
            message = Students.StudentMessage.parseFrom(event);
        }
        catch (InvalidProtocolBufferException e) {
            log.warn("Malformed message {}", event, e);
        }
        try {
            val command = restMapper.messageToCommand(message);
            createStudentUseCase.create(command);
        }
        catch (Exception e) {
            log.error("Unexpected Error while consuming message {}", message, e);
        }
    }

    @Override
    @KafkaListener(id = EXEC + "_course", topics = "course-created")
    public void consumeMessageForCourseCreated(final byte[] event)
    {
        Courses.CourseMessage message = null;
        try {
            message = Courses.CourseMessage.parseFrom(event);
        }
        catch (InvalidProtocolBufferException e) {
            log.warn("Malformed message {}", event, e);
        }
        try {
            val command = restMapper.messageToCommand(message);
            createCourseUseCase.create(command);
        }
        catch (Exception e) {
            log.error("Unexpected Error while consuming message {}", message, e);
        }
    }
}
