package io.metadata.subscriptions.domain.ports.output;

import io.metadata.api.courses.CourseResponse;
import io.metadata.subscriptions.domain.model.CourseId;

public interface CourseServicePort
{
    CourseResponse getById(CourseId id);
}
