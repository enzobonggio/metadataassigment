package io.metadata.students.domain.services.mapper;

import io.metadata.api.Students;
import io.metadata.api.students.StudentResponse;
import io.metadata.students.domain.model.Student;
import io.metadata.students.domain.model.StudentId;
import io.metadata.students.domain.ports.input.DeleteStudentUseCase;
import io.metadata.students.domain.model.StudentName;
import io.metadata.students.domain.ports.input.CreateStudentUseCase;
import io.metadata.students.domain.ports.input.UpdateStudentUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ServiceMapper
{
    @Mapping(source = "id.value", target = "id")
    Students.StudentMessage studentToMessage(Student student);

    Students.StudentMessage studentIdToMessage(StudentId id);

    Student commandToDomain(UpdateStudentUseCase.Command command);

    StudentResponse domainToResponse(Student student);

    default StudentName mapName(String name)
    {
        return StudentName.of(name);
    }

    default StudentId mapId(Long id)
    {
        return StudentId.of(id);
    }

    default String mapName(StudentName name)
    {
        return name.getValue();
    }

    default Long mapId(StudentId id)
    {
        return id.getValue();
    }

    default StudentName commandToStudentName(CreateStudentUseCase.Command command)
    {
        return StudentName.of(command.getName());
    }

    default StudentId commandToStudentId(DeleteStudentUseCase.Command command)
    {
        return StudentId.of(command.getId());
    }
}
