package io.metadata.subscriptions.domain.services;

import io.metadata.api.subscriptions.StudentResponse;
import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchLazyStudentUseCase;
import io.metadata.subscriptions.domain.ports.output.CourseServicePort;
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort;
import io.metadata.subscriptions.domain.ports.output.StudentServicePort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper;
import io.micrometer.core.annotation.Timed;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class StudentService implements
    CreateStudentUseCase,
    DeleteStudentUseCase,
    FetchLazyStudentUseCase
{
    final StudentOutputPort studentOutputPort;
    final SubscriptionMessageSender subscriptionMessageSender;
    final StudentServicePort studentServicePort;
    final ServiceMapper mapper;

    @Override
    @Timed
    public Long create(final CreateStudentUseCase.Command command)
    {
        val id = mapper.commandToStudentId(command);
        studentOutputPort.save(id);
        val message = mapper.idToMessage(id);
        subscriptionMessageSender.publishMessageForStudentCreated(message);
        return id.getValue();
    }

    @Override
    @Timed
    public Long delete(final DeleteStudentUseCase.Command command)
    {
        val id = mapper.commandToStudentId(command);
        studentOutputPort.delete(id);
        return id.getValue();
    }

    @Override
    @Timed
    public Collection<StudentResponse> fetchLazyStudent()
    {
        return studentOutputPort.fetchLazy().stream()
            .map(studentServicePort::getById)
            .map(mapper::studentResponseToResponse)
            .collect(Collectors.toList());
    }
}
