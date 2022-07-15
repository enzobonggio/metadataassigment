package io.metadata.subscriptions.domain.ports.output;

import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.model.Subscription;
import java.util.Collection;

public interface SubscriptionOutputPort
{
    Subscription save(Subscription subscription);

    Collection<Subscription> fetchByStudentId(StudentId id);

    Collection<Subscription> fetchByCourseId(CourseId id);

}
