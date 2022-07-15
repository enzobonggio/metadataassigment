package io.metadata.api.courses;

import io.metadata.api.commons.State;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CourseIdResponse
{
    long id;
    State state;
}
