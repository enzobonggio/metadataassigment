package io.metadata.subscriptions.domain.ports.input;

public interface SubscriptionMessageConsumer
{
    void consumeMessageForStudentCreated(byte[] event);
    void consumeMessageForStudentDeleted(final byte[] event);

    void consumeMessageForCourseCreated(byte[] event);
    void consumeMessageForCourseDeleted(final byte[] event);

}
