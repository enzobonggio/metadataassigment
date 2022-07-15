package io.metadata.students.domain.ports.input;

import io.metadata.api.students.StudentResponse;
import java.util.Collection;

public interface FetchStudentUseCase
{

    Collection<StudentResponse> fetch();
}
