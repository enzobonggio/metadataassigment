package io.metadata.students.domain.services.mapper;

import io.metadata.api.Courses;
import io.metadata.api.courses.CourseResponse;
import io.metadata.students.domain.model.Course;
import io.metadata.students.domain.model.CourseId;
import io.metadata.students.domain.model.CourseName;
import io.metadata.students.domain.ports.input.CreateCourseUseCase;
import io.metadata.students.domain.ports.input.DeleteCourseUseCase;
import io.metadata.students.domain.ports.input.UpdateCourseUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ServiceMapper
{
    @Mapping(source = "id.value", target = "id")
    Courses.CourseMessage courseToMessage(Course course);

    Courses.CourseMessage courseIdToMessage(CourseId id);

    Course commandToDomain(UpdateCourseUseCase.Command command);

    CourseResponse domainToResponse(Course course);

    default CourseName mapName(String name)
    {
        return CourseName.of(name);
    }

    default CourseId mapId(Long id)
    {
        return CourseId.of(id);
    }

    default String mapName(CourseName name)
    {
        return name.getValue();
    }

    default Long mapId(CourseId id)
    {
        return id.getValue();
    }

    default CourseName commandToCourseName(CreateCourseUseCase.Command command)
    {
        return CourseName.of(command.getName());
    }

    default CourseId commandToCourseId(DeleteCourseUseCase.Command command)
    {
        return CourseId.of(command.getId());
    }
}
