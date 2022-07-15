package io.metadata.subscriptions.domain.services;

import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class StudentService implements
    CreateStudentUseCase
{
    final StudentOutputPort studentOutputPort;
    final SubscriptionMessageSender subscriptionMessageSender;
    final ServiceMapper mapper;

    @Override
    public Long create(final CreateStudentUseCase.Command command)
    {
        val id = mapper.commandToStudentId(command);
        studentOutputPort.save(id);
        val message = mapper.idToMessage(id);
        subscriptionMessageSender.publishMessageForStudentCreated(message);
        return id.getValue();
    }
}
