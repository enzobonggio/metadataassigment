package io.metadata.courses.domain.ports.input;

public interface CourseMessageConsumer
{
    void consumeMessageForSubscriptionCourseCreated(byte[] event);
}
