package io.metadata.students.domain.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class StudentName
{
    String value;
}
