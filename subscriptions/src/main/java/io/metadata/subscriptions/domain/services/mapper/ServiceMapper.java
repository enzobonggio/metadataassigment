package io.metadata.subscriptions.domain.services.mapper;

import io.metadata.api.Subscriptions;
import io.metadata.api.students.StudentResponse;
import io.metadata.api.subscriptions.CourseResponse;
import io.metadata.api.subscriptions.SubscriptionResponse;
import io.metadata.api.subscriptions.SubscriptionsResponse;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.model.Subscription;
import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ServiceMapper
{
    @Mapping(source = "courseId.value", target = "courseId")
    @Mapping(source = "studentId.value", target = "studentId")
    SubscriptionResponse domainToResponse(Subscription savedSubscription);

    Subscription commandToDomain(SubscribeToCourseUseCase.Command command);

    @Mapping(source = "value", target = "id")
    Subscriptions.StudentMessage idToMessage(StudentId id);

    @Mapping(source = "value", target = "id")
    Subscriptions.CourseMessage idToMessage(CourseId id);

    @Mappings({
        @Mapping(source = "courseResponse.name", target = "name"),
        @Mapping(source = "courseResponse.id", target = "id"),
        @Mapping(source = "studentResponses", target = "students")
    })
    CourseResponse entryToCourseResponse(io.metadata.api.courses.CourseResponse courseResponse, List<StudentResponse> studentResponses);

    io.metadata.api.subscriptions.StudentResponse entryToCourseResponse(StudentResponse studentResponse);

    default StudentId mapStudentId(Long id)
    {
        return StudentId.of(id);
    }

    default CourseId mapCourseId(Long id)
    {
        return CourseId.of(id);
    }

    default CourseId commandToCourseId(CreateCourseUseCase.Command command)
    {
        return mapCourseId(command.getId());
    }

    default StudentId commandToStudentId(CreateStudentUseCase.Command command)
    {
        return mapStudentId(command.getId());
    }

    default SubscriptionsResponse coursesResponsesToSubscriptionsResponse(List<CourseResponse> courseResponses)
    {
        return SubscriptionsResponse.builder().courses(courseResponses).build();
    }
}
