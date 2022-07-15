package io.metadata.subscriptions.domain.ports.input;

import io.metadata.api.subscriptions.SubscriptionsResponse;

public interface FetchSubscriptionsByStudentUseCase
{

    SubscriptionsResponse getByStudentId(Long studentId);
}