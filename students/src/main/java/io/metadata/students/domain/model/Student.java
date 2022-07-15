package io.metadata.students.domain.model;

import io.metadata.api.commons.State;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Student
{
    StudentId id;
    StudentName name;
    State state;
}
