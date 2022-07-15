package io.metadata.api.students;

import io.metadata.api.commons.State;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StudentIdResponse
{
    long id;
    State state;
}
