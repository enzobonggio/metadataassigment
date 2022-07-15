package io.metadata.subscriptions.domain.ports.output;

import io.metadata.api.students.StudentResponse;
import io.metadata.subscriptions.domain.model.StudentId;

public interface StudentServicePort
{
    StudentResponse getById(StudentId id);
}
