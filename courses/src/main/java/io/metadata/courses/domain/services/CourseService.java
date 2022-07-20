package io.metadata.courses.domain.services;

import io.metadata.api.courses.CourseResponse;
import io.metadata.courses.domain.model.Course;
import io.metadata.courses.domain.ports.input.CreateCourseUseCase;
import io.metadata.courses.domain.ports.input.DeleteCourseUseCase;
import io.metadata.courses.domain.ports.input.FetchCourseUseCase;
import io.metadata.courses.domain.ports.input.GetCourseUseCase;
import io.metadata.courses.domain.ports.input.UpdateCourseStateUseCase;
import io.metadata.courses.domain.ports.input.UpdateCourseUseCase;
import io.metadata.courses.domain.ports.output.CourseMessageSender;
import io.metadata.courses.domain.ports.output.CourseOutputPort;
import io.metadata.courses.domain.services.mapper.ServiceMapper;
import io.micrometer.core.annotation.Timed;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CourseService implements
    CreateCourseUseCase,
    UpdateCourseUseCase,
    GetCourseUseCase,
    FetchCourseUseCase,
    DeleteCourseUseCase,
    UpdateCourseStateUseCase
{
    final CourseOutputPort courseOutputPort;
    final CourseMessageSender courseEventPublisher;
    final ServiceMapper mapper;

    @Override
    @Transactional
    @Timed
    public Long create(final CreateCourseUseCase.Command command)
    {
        val name = mapper.commandToCourseName(command);
        val course = courseOutputPort.save(name);
        val createdEvent = mapper.courseToMessage(course);
        courseEventPublisher.publishMessageForCreated(createdEvent);
        return course.getId().getValue();
    }

    @Override
    @Transactional
    @Timed
    public void delete(final DeleteCourseUseCase.Command command)
    {
        getById(command.getId());
        val id = mapper.commandToCourseId(command);
        courseOutputPort.deleteById(id);
        val deletedEvent = mapper.courseIdToMessage(id);
        courseEventPublisher.publishMessageForDeleted(deletedEvent);
    }

    @Override
    @Timed
    public Collection<CourseResponse> fetch()
    {
        return courseOutputPort.fetch().stream()
            .map(mapper::domainToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Timed
    public CourseResponse getById(final Long id)
    {
        val course = courseOutputPort.getById(mapper.mapId(id));
        return mapper.domainToResponse(course);
    }

    @Override
    @Timed
    public CourseResponse update(final UpdateCourseUseCase.Command command)
    {
        final Course course = mapper.commandToDomain(command);
        courseOutputPort.update(course);
        return getById(command.getId());
    }

    @Override
    @Timed
    public CourseResponse updateState(final UpdateCourseStateUseCase.Command command)
    {
        val idValue = command.getId();
        val id = mapper.mapId(idValue);
        courseOutputPort.updateState(id, command.getState());
        return getById(idValue);
    }
}
