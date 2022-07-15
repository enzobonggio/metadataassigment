package io.metadata.api.subscriptions;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StudentResponse
{
    long id;
    String name;
}
