package io.metadata.students.domain.ports.input;

import io.metadata.api.courses.CourseResponse;

public interface GetCourseUseCase
{

    CourseResponse getById(Long id);
}
