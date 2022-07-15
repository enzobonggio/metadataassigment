package io.metadata.students.domain.ports.input;

import io.metadata.api.students.StudentResponse;

public interface GetStudentUseCase
{

    StudentResponse getById(Long id);
}
