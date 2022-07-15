package io.metadata.students.adapters.output.messagesender;

import io.metadata.api.Courses;
import io.metadata.students.domain.ports.output.CourseMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class CourseMessageSenderAdapter implements CourseMessageSender
{
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Override
    public void publishMessageForCreated(final Courses.CourseMessage event)
    {
        kafkaTemplate.send("course-created", event.toByteArray());
    }

    @Override
    public void publishMessageForDeleted(final Courses.CourseMessage event)
    {
        kafkaTemplate.send("course-deleted", event.toByteArray());
    }
}
