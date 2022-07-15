package io.metadata.students.adapters.output.messagesender;

import io.metadata.api.Courses;
import io.metadata.students.domain.ports.output.CourseMessageSender;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryCourseMessageSenderAdapter implements CourseMessageSender
{
    @Override
    public void publishMessageForCreated(final Courses.CourseMessage event)
    {
    }

    @Override
    public void publishMessageForDeleted(final Courses.CourseMessage event)
    {
    }
}
