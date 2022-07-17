package io.metadata.courses.domain.ports.input;

import lombok.Builder;
import lombok.Value;

public interface CreateCourseUseCase
{

    Long create(Command command);

    @Builder
    @Value
    class Command {
        String name;
    }
}
