package io.metadata.subscriptions.domain.ports.output;

import io.metadata.subscriptions.domain.model.CourseId;

public interface CourseOutputPort
{
    CourseId save(CourseId id);
}
