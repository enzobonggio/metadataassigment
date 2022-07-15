package io.metadata.students.adapters.mapper;

import io.metadata.api.Subscriptions;
import io.metadata.api.students.StudentRequest;
import io.metadata.students.domain.model.StudentId;
import io.metadata.students.domain.model.StudentName;
import io.metadata.students.domain.ports.input.CreateStudentUseCase;
import io.metadata.students.domain.ports.input.DeleteStudentUseCase;
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase;
import io.metadata.students.domain.ports.input.UpdateStudentUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AdapterMapper
{

    CreateStudentUseCase.Command requestToCreateCommand(StudentRequest request);

    DeleteStudentUseCase.Command requestToDeleteCommand(Long id);

    UpdateStudentUseCase.Command requestToUpdateCommand(Long id, StudentRequest request);

    @Mapping(target = "state", expression = "java(io.metadata.api.commons.State.CREATED)")
    UpdateStudentStateUseCase.Command messageToCommand(Subscriptions.StudentMessage message);

    default String mapName(StudentName name)
    {
        return name.getValue();
    }

    default Long mapId(StudentId id)
    {
        return id.getValue();
    }
}
