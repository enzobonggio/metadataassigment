package io.metadata.students.domain.ports.input;

import lombok.Builder;
import lombok.Value;

public interface CreateStudentUseCase
{

    Long create(Command command);

    @Builder
    @Value
    class Command {
        String name;
    }
}
