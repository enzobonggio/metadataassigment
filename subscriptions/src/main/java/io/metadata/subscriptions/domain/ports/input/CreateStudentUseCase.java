package io.metadata.subscriptions.domain.ports.input;

import lombok.Builder;
import lombok.Value;

public interface CreateStudentUseCase
{

    Long create(Command command);

    @Builder
    @Value
    class Command {
        Long id;
    }
}
