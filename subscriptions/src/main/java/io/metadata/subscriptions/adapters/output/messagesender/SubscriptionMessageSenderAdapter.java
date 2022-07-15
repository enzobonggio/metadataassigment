package io.metadata.subscriptions.adapters.output.messagesender;

import io.metadata.api.Subscriptions;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class SubscriptionMessageSenderAdapter implements SubscriptionMessageSender
{
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Override
    public void publishMessageForStudentCreated(final Subscriptions.StudentMessage event)
    {
        kafkaTemplate.send("subscription-student-created", event.toByteArray());
    }

    @Override
    public void publishMessageForCourseCreated(final Subscriptions.CourseMessage event)
    {
        kafkaTemplate.send("subscription-course-created", event.toByteArray());
    }
}
