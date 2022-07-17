package io.metadata.courses.domain.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class CourseName
{
    String value;
}
