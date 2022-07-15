package io.metadata.subscriptions.domain.services;

import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class CourseService implements
    CreateCourseUseCase
{
    final CourseOutputPort courseOutputPort;
    final SubscriptionMessageSender subscriptionMessageSender;
    final ServiceMapper mapper;

    @Override
    public Long create(final Command command)
    {
        val id = mapper.commandToCourseId(command);
        courseOutputPort.save(id);
        val message = mapper.idToMessage(id);
        subscriptionMessageSender.publishMessageForCourseCreated(message);
        return id.getValue();
    }
}
