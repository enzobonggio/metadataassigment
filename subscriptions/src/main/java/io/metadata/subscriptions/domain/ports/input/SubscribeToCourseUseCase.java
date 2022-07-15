package io.metadata.subscriptions.domain.ports.input;

import io.metadata.api.subscriptions.SubscriptionResponse;
import lombok.Builder;
import lombok.Value;

public interface SubscribeToCourseUseCase
{

    SubscriptionResponse subscribe(Command command);

    @Builder
    @Value
    class Command
    {
        long courseId;
        long studentId;
    }
}
