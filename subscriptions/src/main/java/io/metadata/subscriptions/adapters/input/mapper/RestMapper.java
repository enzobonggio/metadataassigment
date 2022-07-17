package io.metadata.subscriptions.adapters.input.mapper;

import io.metadata.api.Courses;
import io.metadata.api.Students;
import io.metadata.api.subscriptions.SubscriptionRequest;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase;
import org.mapstruct.Mapper;

@Mapper
public interface RestMapper
{
    SubscribeToCourseUseCase.Command requestToSubscribeToCourseCommand(SubscriptionRequest courseId);

    CreateStudentUseCase.Command messageToCreateCommand(Students.StudentMessage event);
    DeleteStudentUseCase.Command messageToDeleteCommand(Students.StudentMessage event);

    CreateCourseUseCase.Command messageToCreateCommand(Courses.CourseMessage event);
    DeleteCourseUseCase.Command messageToDeleteCommand(Courses.CourseMessage event);

    default StudentId mapStudentLong(Long id)
    {
        return StudentId.of(id);
    }
    default CourseId mapCourseLong(Long id)
    {
        return CourseId.of(id);
    }

}
