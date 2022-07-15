package io.metadata.students.adapters.output.messagesender;

import io.metadata.api.Students;
import io.metadata.students.domain.ports.output.StudentMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class StudentMessageSenderAdapter implements StudentMessageSender
{
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Override
    public void publishMessageForCreated(final Students.StudentMessage event)
    {
        kafkaTemplate.send("student-created", event.toByteArray());
    }

    @Override
    public void publishMessageForDeleted(final Students.StudentMessage event)
    {
        kafkaTemplate.send("student-deleted", event.toByteArray());
    }
}
