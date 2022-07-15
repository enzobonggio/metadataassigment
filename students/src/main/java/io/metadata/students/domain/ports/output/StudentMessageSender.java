package io.metadata.students.domain.ports.output;

import io.metadata.api.Students;

public interface StudentMessageSender
{
    void publishMessageForCreated(Students.StudentMessage event);

    void publishMessageForDeleted(Students.StudentMessage event);
}
