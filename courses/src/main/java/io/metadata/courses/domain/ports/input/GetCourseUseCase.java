package io.metadata.courses.domain.ports.input;

import io.metadata.api.courses.CourseResponse;

public interface GetCourseUseCase
{

    CourseResponse getById(Long id);
}
