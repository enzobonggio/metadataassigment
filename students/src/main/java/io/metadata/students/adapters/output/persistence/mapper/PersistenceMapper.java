package io.metadata.students.adapters.output.persistence.mapper;

import io.metadata.students.adapters.output.persistence.entity.StudentEntity;
import io.metadata.students.domain.model.Student;
import io.metadata.students.domain.model.StudentId;
import io.metadata.students.domain.model.StudentName;
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
    StudentEntity domainToEntity(Student student);

    @Mappings({
        @Mapping(expression = "java(now())", target = "createdAt"),
        @Mapping(expression = "java(now())", target = "lastModifiedAt"),
        @Mapping(source = "value", target = "name"),
        @Mapping(target = "state", expression = "java(io.metadata.api.commons.State.CREATING.toString())"),
        @Mapping(ignore = true, target = "id")
    })
    StudentEntity domainToEntity(StudentName studentName);

    Student entityToDomain(StudentEntity entity);

    default Timestamp now()
    {
        return Timestamp.from(Instant.now());
    }

    default StudentName mapName(String name)
    {
        return StudentName.of(name);
    }

    default StudentId mapId(Long id)
    {
        return StudentId.of(id);
    }
}
