package io.metadata.students.adapters.output.messagesender;

import io.metadata.api.Students;
import io.metadata.students.domain.ports.output.StudentMessageSender;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryStudentMessageSenderAdapter implements StudentMessageSender
{
    @Override
    public void publishMessageForCreated(final Students.StudentMessage event)
    {
    }

    @Override
    public void publishMessageForDeleted(final Students.StudentMessage event)
    {
    }
}
