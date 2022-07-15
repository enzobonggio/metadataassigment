package io.metadata.students.domain.ports.input;

import io.metadata.api.commons.State;
import lombok.Builder;
import lombok.Value;

public interface UpdateCourseStateUseCase
{

    Long updateState(Command command);

    @Value
    @Builder
    class Command {
        Long id;
        State state;
    }
}
