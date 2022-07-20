package io.metadata.subscriptions.domain.ports.output;

import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.Student;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.model.Subscription;
import java.util.Collection;

public interface SubscriptionOutputPort
{
    Subscription save(Subscription subscription);

    Long countByStudentId(StudentId id);

    Collection<Subscription> fetchByStudentId(StudentId id);

    Long countByCourseId(CourseId id);

    Collection<Subscription> fetchByCourseId(CourseId id);
}
