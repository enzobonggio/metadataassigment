package io.metadata.subscriptions.domain.ports.input;

import io.metadata.api.students.StudentResponse;
import java.util.Collection;

public interface FetchLazyStudentUseCase
{

    Collection<StudentResponse> fetchLazyStudent();
}
