package io.metadata.subscriptions.domain.ports.input;

import io.metadata.api.subscriptions.StudentResponse;
import java.util.Collection;

public interface FetchLazyStudentUseCase
{

    Collection<StudentResponse> fetchLazyStudent();
}
