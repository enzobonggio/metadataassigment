package io.metadata.subscriptions.domain.ports.input;

public interface SubscriptionMessageConsumer
{
    void consumeMessageForStudentCreated(byte[] event);

    void consumeMessageForCourseCreated(byte[] event);
}
