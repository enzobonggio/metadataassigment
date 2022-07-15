package io.metadata.subscriptions.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Student
{
    StudentId id;
    StudentName name;
}
