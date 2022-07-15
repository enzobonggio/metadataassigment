package io.metadata.subscriptions.domain.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class StudentId
{
    Long value;
}
