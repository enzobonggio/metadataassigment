package io.metadata.subscriptions.domain.ports.output;

import io.metadata.subscriptions.domain.model.StudentId;

public interface StudentOutputPort
{
    StudentId save(StudentId id);
}
