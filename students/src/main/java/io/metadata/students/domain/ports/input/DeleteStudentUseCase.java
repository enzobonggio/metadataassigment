package io.metadata.students.domain.ports.input;

import lombok.Builder;
import lombok.Value;

public interface DeleteStudentUseCase
{

    void delete(Command command);

    @Value
    @Builder
    class Command {
        Long id;
    }
}
