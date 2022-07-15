package io.metadata.subscriptions.adapters.input.mapper;

import io.metadata.api.Courses;
import io.metadata.api.Students;
import io.metadata.api.subscriptions.SubscriptionRequest;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase;
import org.mapstruct.Mapper;

@Mapper
public interface RestMapper
{
    SubscribeToCourseUseCase.Command requestToSubscribeToCourseCommand(SubscriptionRequest courseId);

    CreateStudentUseCase.Command messageToCommand(Students.StudentMessage event);

    CreateCourseUseCase.Command messageToCommand(Courses.CourseMessage message);

    default StudentId mapStudentLong(Long id)
    {
        return StudentId.of(id);
    }
    default CourseId mapCourseLong(Long id)
    {
        return CourseId.of(id);
    }

}
