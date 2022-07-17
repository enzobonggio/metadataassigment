package io.metadata.courses.domain.ports.input;

import lombok.Builder;
import lombok.Value;

public interface DeleteCourseUseCase
{

    void delete(Command command);

    @Value
    @Builder
    class Command {
        Long id;
    }
}
