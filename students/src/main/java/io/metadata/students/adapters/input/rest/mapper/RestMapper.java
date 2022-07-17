package io.metadata.students.adapters.input.rest.mapper;

import io.metadata.api.students.StudentRequest;
import io.metadata.students.domain.ports.input.CreateStudentUseCase;
import io.metadata.students.domain.ports.input.DeleteStudentUseCase;
import io.metadata.students.domain.ports.input.UpdateStudentUseCase;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RestMapper
{

    CreateStudentUseCase.Command requestToCreateCommand(StudentRequest request);

    DeleteStudentUseCase.Command requestToDeleteCommand(Long id);

    UpdateStudentUseCase.Command requestToUpdateCommand(Long id, StudentRequest request);
}
