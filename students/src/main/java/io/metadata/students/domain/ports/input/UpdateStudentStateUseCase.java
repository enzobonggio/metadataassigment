package io.metadata.students.domain.ports.input;

import io.metadata.api.commons.State;
import io.metadata.api.students.StudentResponse;
import lombok.Builder;
import lombok.Value;

public interface UpdateStudentStateUseCase
{

    StudentResponse updateState(Command command);

    @Value
    @Builder
    class Command {
        Long id;
        State state;
    }
}
