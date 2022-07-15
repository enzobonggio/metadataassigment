package io.metadata.api.subscriptions;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CourseResponse
{
    long id;
    String name;
    Iterable<StudentResponse> students;
}
