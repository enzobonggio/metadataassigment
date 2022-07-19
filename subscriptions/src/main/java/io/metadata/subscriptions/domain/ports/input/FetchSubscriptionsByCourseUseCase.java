package io.metadata.subscriptions.domain.ports.input;

import io.metadata.api.subscriptions.SubscriptionsResponse;

public interface FetchSubscriptionsByCourseUseCase
{

    SubscriptionsResponse fetchByCourseId(Long courseId);
}
