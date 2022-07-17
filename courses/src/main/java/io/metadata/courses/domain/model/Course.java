package io.metadata.courses.domain.model;

import io.metadata.api.commons.State;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Course
{
    CourseId id;
    CourseName name;
    State state;
}
