package io.metadata.students.domain.ports.input;

import io.metadata.api.students.StudentResponse;
import lombok.Builder;
import lombok.Value;

public interface UpdateStudentUseCase
{

    StudentResponse update(Command command);

    @Value
    @Builder
    class Command {
        Long id;
        String name;
    }
}
