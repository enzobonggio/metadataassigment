package io.metadata.students.domain.ports.input;

import io.metadata.api.courses.CourseResponse;
import lombok.Builder;
import lombok.Value;

public interface UpdateCourseUseCase
{

    CourseResponse update(Command command);

    @Value
    @Builder
    class Command {
        Long id;
        String name;
    }
}
