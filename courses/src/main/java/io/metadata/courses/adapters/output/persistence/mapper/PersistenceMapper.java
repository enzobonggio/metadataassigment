package io.metadata.courses.adapters.output.persistence.mapper;

import io.metadata.courses.adapters.output.persistence.entity.CourseEntity;
import io.metadata.courses.domain.model.Course;
import io.metadata.courses.domain.model.CourseId;
import io.metadata.courses.domain.model.CourseName;
import java.sql.Timestamp;
import java.time.Instant;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersistenceMapper
{

    @Mappings({
        @Mapping(expression = "java(now())", target = "lastModifiedAt"),
        @Mapping(source = "name.value", target = "name"),
        @Mapping(source = "id.value", target = "id"),
    })
    CourseEntity domainToEntity(Course course);

    @Mappings({
        @Mapping(expression = "java(now())", target = "createdAt"),
        @Mapping(expression = "java(now())", target = "lastModifiedAt"),
        @Mapping(source = "value", target = "name"),
        @Mapping(target = "state", expression = "java(io.metadata.api.commons.State.CREATING.toString())"),
        @Mapping(ignore = true, target = "id")
    })
    CourseEntity domainToEntity(CourseName courseName);

    Course entityToDomain(CourseEntity entity);

    default Timestamp now()
    {
        return Timestamp.from(Instant.now());
    }

    default CourseName mapName(String name)
    {
        return CourseName.of(name);
    }

    default CourseId mapId(Long id)
    {
        return CourseId.of(id);
    }
}
