package io.metadata.students.domain.services;

import io.metadata.api.students.StudentResponse;
import io.metadata.students.domain.model.Student;
import io.metadata.students.domain.ports.input.CreateStudentUseCase;
import io.metadata.students.domain.ports.input.DeleteStudentUseCase;
import io.metadata.students.domain.ports.input.FetchStudentUseCase;
import io.metadata.students.domain.ports.input.GetStudentUseCase;
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase;
import io.metadata.students.domain.ports.input.UpdateStudentUseCase;
import io.metadata.students.domain.ports.output.StudentMessageSender;
import io.metadata.students.domain.ports.output.StudentOutputPort;
import io.metadata.students.domain.services.mapper.ServiceMapper;
import io.micrometer.core.annotation.Timed;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class StudentService implements
    CreateStudentUseCase,
    UpdateStudentUseCase,
    GetStudentUseCase,
    FetchStudentUseCase,
    DeleteStudentUseCase,
    UpdateStudentStateUseCase
{
    final StudentOutputPort studentOutputPort;
    final StudentMessageSender studentEventPublisher;
    final ServiceMapper mapper;

    @Override
    @Transactional
    @Timed
    public Long create(final CreateStudentUseCase.Command command)
    {
        val name = mapper.commandToStudentName(command);
        val student = studentOutputPort.save(name);
        val createdEvent = mapper.studentToMessage(student);
        studentEventPublisher.publishMessageForCreated(createdEvent);
        return student.getId().getValue();
    }

    @Override
    @Transactional
    @Timed
    public void delete(final DeleteStudentUseCase.Command command)
    {
        val id = mapper.commandToStudentId(command);
        studentOutputPort.deleteById(id);
        val deletedEvent = mapper.studentIdToMessage(id);
        studentEventPublisher.publishMessageForDeleted(deletedEvent);
    }

    @Override
    @Timed
    public Collection<StudentResponse> fetch()
    {
        return studentOutputPort.fetch().stream()
            .map(mapper::domainToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Timed
    public StudentResponse getById(final Long id)
    {
        val student = studentOutputPort.getById(mapper.mapId(id));
        return mapper.domainToResponse(student);
    }

    @Override
    @Timed
    public StudentResponse update(final UpdateStudentUseCase.Command command)
    {
        final Student student = mapper.commandToDomain(command);
        studentOutputPort.update(student);
        return getById(command.getId());
    }

    @Override
    @Timed
    public StudentResponse updateState(final UpdateStudentStateUseCase.Command command)
    {
        val idValue = command.getId();
        val id = mapper.mapId(idValue);
        studentOutputPort.updateState(id, command.getState());
        return getById(idValue);
    }
}
