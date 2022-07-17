package io.metadata.courses.domain.ports.output;

import io.metadata.api.Courses;

public interface CourseMessageSender
{
    void publishMessageForCreated(Courses.CourseMessage event);

    void publishMessageForDeleted(Courses.CourseMessage event);
}
