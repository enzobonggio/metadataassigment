package io.metadata.subscriptions.domain.ports.output;

import io.metadata.api.Subscriptions;

public interface SubscriptionMessageSender
{
    void publishMessageForStudentCreated(Subscriptions.StudentMessage event);
    void publishMessageForCourseCreated(Subscriptions.CourseMessage event);

}
