package io.metadata.subscriptions.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Subscription
{
    CourseId courseId;
    StudentId studentId;
}
