package io.metadata.courses.adapters.input.rest.mapper;

import io.metadata.api.Subscriptions;
import io.metadata.api.courses.CourseRequest;
import io.metadata.courses.domain.ports.input.CreateCourseUseCase;
import io.metadata.courses.domain.ports.input.DeleteCourseUseCase;
import io.metadata.courses.domain.ports.input.UpdateCourseStateUseCase;
import io.metadata.courses.domain.ports.input.UpdateCourseUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RestMapper
{

    CreateCourseUseCase.Command requestToCreateCommand(CourseRequest request);

    DeleteCourseUseCase.Command requestToDeleteCommand(Long id);

    UpdateCourseUseCase.Command requestToUpdateCommand(Long id, CourseRequest request);
}
