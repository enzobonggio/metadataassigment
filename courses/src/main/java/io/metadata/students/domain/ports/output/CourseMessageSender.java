package io.metadata.students.domain.ports.output;

import io.metadata.api.Courses;

public interface CourseMessageSender
{
    void publishMessageForCreated(Courses.CourseMessage event);

    void publishMessageForDeleted(Courses.CourseMessage event);
}
