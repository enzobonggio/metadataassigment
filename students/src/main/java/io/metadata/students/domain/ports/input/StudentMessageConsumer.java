package io.metadata.students.domain.ports.input;

public interface StudentMessageConsumer
{
    void consumeMessageForSubscriptionStudentCreated(byte[] event);
}
