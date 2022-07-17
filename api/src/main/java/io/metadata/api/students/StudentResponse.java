package io.metadata.api.students;

import io.metadata.api.commons.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudentResponse
{
    long id;
    String name;
    State state;
}
