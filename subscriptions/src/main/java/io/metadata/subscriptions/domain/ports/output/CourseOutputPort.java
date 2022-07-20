package io.metadata.subscriptions.domain.ports.output;

import io.metadata.subscriptions.domain.model.CourseId;
import java.util.Collection;

public interface CourseOutputPort
{
    CourseId save(CourseId id);

    CourseId delete(CourseId id);

    Collection<CourseId> fetchEmpty();

    Boolean exists(CourseId id);
}
