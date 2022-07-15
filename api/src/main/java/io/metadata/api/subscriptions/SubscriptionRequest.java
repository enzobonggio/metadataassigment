package io.metadata.api.subscriptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SubscriptionRequest
{
    long courseId;
    long studentId;
}
