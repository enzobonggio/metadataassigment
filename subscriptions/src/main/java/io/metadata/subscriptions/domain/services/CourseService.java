package io.metadata.subscriptions.domain.services;

import io.metadata.api.courses.CourseResponse;
import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchEmptyCourseUseCase;
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort;
import io.metadata.subscriptions.domain.ports.output.CourseServicePort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper;
import io.micrometer.core.annotation.Timed;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@RequiredArgsConstructor
@Slf4j
public class CourseService implements
    CreateCourseUseCase,
    DeleteCourseUseCase,
    FetchEmptyCourseUseCase
{
    final CourseOutputPort courseOutputPort;
    final SubscriptionMessageSender subscriptionMessageSender;
    final CourseServicePort courseServicePort;
    final ServiceMapper mapper;

    @Override
    @Timed
    public Long create(final CreateCourseUseCase.Command command)
    {
        val id = mapper.commandToCourseId(command);
        courseOutputPort.save(id);
        val message = mapper.idToMessage(id);
        subscriptionMessageSender.publishMessageForCourseCreated(message);
        return id.getValue();
    }

    @Override
    @Timed
    public Long delete(final DeleteCourseUseCase.Command command)
    {
        val id = mapper.commandToCourseId(command);
        courseOutputPort.delete(id);
        return id.getValue();
    }

    @Override
    @Timed
    public Collection<CourseResponse> fetchEmptyCourse()
    {
        return courseOutputPort.fetchEmpty().stream()
            .map(courseServicePort::getById)
            .collect(Collectors.toList());
    }
}
