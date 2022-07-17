package io.metadata.courses.domain.ports.input;

import io.metadata.api.commons.State;
import io.metadata.api.courses.CourseResponse;
import lombok.Builder;
import lombok.Value;

public interface UpdateCourseStateUseCase
{

    CourseResponse updateState(Command command);

    @Value
    @Builder
    class Command {
        Long id;
        State state;
    }
}
