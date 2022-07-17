package io.metadata.api.courses;

import io.metadata.api.commons.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CourseResponse
{
    long id;
    String name;
    State state;
}
