package io.metadata.subscriptions.domain.ports.input;

import io.metadata.api.courses.CourseResponse;
import java.util.Collection;

public interface FetchEmptyCourseUseCase
{

    Collection<CourseResponse> fetchEmptyCourse();
}
