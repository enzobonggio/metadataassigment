package io.metadata.courses.domain.ports.input;

import io.metadata.api.courses.CourseResponse;
import java.util.Collection;

public interface FetchCourseUseCase
{

    Collection<CourseResponse> fetch();
}
