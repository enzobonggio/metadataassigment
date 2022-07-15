package io.metadata.students.adapters.mapper;

import io.metadata.api.Subscriptions;
import io.metadata.api.courses.CourseRequest;
import io.metadata.students.domain.model.CourseId;
import io.metadata.students.domain.model.CourseName;
import io.metadata.students.domain.ports.input.CreateCourseUseCase;
import io.metadata.students.domain.ports.input.DeleteCourseUseCase;
import io.metadata.students.domain.ports.input.UpdateCourseStateUseCase;
import io.metadata.students.domain.ports.input.UpdateCourseUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AdapterMapper
{

    CreateCourseUseCase.Command requestToCreateCommand(CourseRequest request);

    DeleteCourseUseCase.Command requestToDeleteCommand(Long id);

    UpdateCourseUseCase.Command requestToUpdateCommand(Long id, CourseRequest request);

    @Mapping(target = "state", expression = "java(io.metadata.api.commons.State.CREATED)")
    UpdateCourseStateUseCase.Command messageToCommand(Subscriptions.CourseMessage message);

    default String mapName(CourseName name)
    {
        return name.getValue();
    }

    default Long mapId(CourseId id)
    {
        return id.getValue();
    }
}
