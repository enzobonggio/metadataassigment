package io.metadata.courses.adapters.output.messagesender;

import io.metadata.api.Courses;
import io.metadata.courses.domain.ports.output.CourseMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Slf4j
public class CourseMessageSenderAdapter implements CourseMessageSender
{
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Override
    public void publishMessageForCreated(final Courses.CourseMessage event)
    {
        log.info("publishing course-created message");
        kafkaTemplate.send("course-created", event.toByteArray());
    }

    @Override
    public void publishMessageForDeleted(final Courses.CourseMessage event)
    {
        log.info("publishing course-deleted message");
        kafkaTemplate.send("course-deleted", event.toByteArray());
    }
}
