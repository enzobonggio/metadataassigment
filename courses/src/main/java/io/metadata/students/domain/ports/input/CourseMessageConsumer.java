package io.metadata.students.domain.ports.input;

public interface CourseMessageConsumer
{
    void consumeMessageForSubscriptionCourseCreated(byte[] event);
}
