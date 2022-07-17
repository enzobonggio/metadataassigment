package io.metadata.subscriptions.domain.ports.input;

import lombok.Builder;
import lombok.Value;

public interface DeleteCourseUseCase
{

    Long delete(Command command);

    @Builder
    @Value
    class Command {
        Long id;
    }
}
