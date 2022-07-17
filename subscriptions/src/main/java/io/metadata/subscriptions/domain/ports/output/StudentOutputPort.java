package io.metadata.subscriptions.domain.ports.output;

import io.metadata.subscriptions.domain.model.StudentId;
import java.util.Collection;

public interface StudentOutputPort
{
    StudentId save(StudentId id);

    StudentId delete(StudentId id);

    Collection<StudentId> fetchLazy();

}
