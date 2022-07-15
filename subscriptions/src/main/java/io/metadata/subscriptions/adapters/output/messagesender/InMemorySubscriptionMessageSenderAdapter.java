package io.metadata.subscriptions.adapters.output.messagesender;

import io.metadata.api.Subscriptions;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemorySubscriptionMessageSenderAdapter implements SubscriptionMessageSender
{

    @Override
    public void publishMessageForStudentCreated(final Subscriptions.StudentMessage event)
    {

    }

    @Override
    public void publishMessageForCourseCreated(final Subscriptions.CourseMessage event)
    {

    }
}
