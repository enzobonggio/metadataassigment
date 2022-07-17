package io.metadata.subscriptions.adapters.input.messageconsumer;

import com.google.protobuf.InvalidProtocolBufferException;
import io.metadata.api.Courses;
import io.metadata.api.Students;
import io.metadata.subscriptions.adapters.input.mapper.RestMapper;
import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscriptionMessageConsumer;
import java.util.Optional;
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
    private final DeleteStudentUseCase deleteStudentUseCase;

    private final CreateCourseUseCase createCourseUseCase;
    private final DeleteCourseUseCase deleteCourseUseCase;

    private final RestMapper restMapper;

    @Override
    @KafkaListener(id = EXEC + "_student-created", topics = "student-created")
    public void consumeMessageForStudentCreated(final byte[] event)
    {
        val message = getStudentMessage(event);
        if (message.isPresent()) {
            try {
                val command = restMapper.messageToCreateCommand(message.get());
                createStudentUseCase.create(command);
            }
            catch (Exception e) {
                log.error("Unexpected Error while consuming message {}", message, e);
            }
        }
    }

    @Override
    @KafkaListener(id = EXEC + "_student-deleted", topics = "student-deleted")
    public void consumeMessageForStudentDeleted(final byte[] event)
    {
        val message = getStudentMessage(event);
        if (message.isPresent()) {
            try {
                val command = restMapper.messageToDeleteCommand(message.get());
                deleteStudentUseCase.delete(command);
            }
            catch (Exception e) {
                log.error("Unexpected Error while consuming message {}", message, e);
            }
        }
    }

    @Override
    @KafkaListener(id = EXEC + "_course-created", topics = "course-created")
    public void consumeMessageForCourseCreated(final byte[] event)
    {
        val message = getCourseMessage(event);
        if (message.isPresent()) {
            try {
                val command = restMapper.messageToCreateCommand(message.get());
                createCourseUseCase.create(command);
            }
            catch (Exception e) {
                log.error("Unexpected Error while consuming message {}", message, e);
            }
        }
    }

    @Override
    @KafkaListener(id = EXEC + "_course-deleted", topics = "course-deleted")
    public void consumeMessageForCourseDeleted(final byte[] event)
    {
        val message = getCourseMessage(event);
        if (message.isPresent()) {
            try {
                val command = restMapper.messageToDeleteCommand(message.get());
                deleteCourseUseCase.delete(command);
            }
            catch (Exception e) {
                log.error("Unexpected Error while consuming message {}", message, e);
            }
        }
    }

    private Optional<Students.StudentMessage> getStudentMessage(final byte[] event)
    {
        Students.StudentMessage message = null;
        try {
            message = Students.StudentMessage.parseFrom(event);
        }
        catch (InvalidProtocolBufferException e) {
            log.warn("Malformed message {}", event, e);
        }
        return Optional.ofNullable(message);
    }

    private Optional<Courses.CourseMessage> getCourseMessage(final byte[] event)
    {
        Courses.CourseMessage message = null;
        try {
            message = Courses.CourseMessage.parseFrom(event);
        }
        catch (InvalidProtocolBufferException e) {
            log.warn("Malformed message {}", event, e);
        }
        return Optional.ofNullable(message);
    }
}
