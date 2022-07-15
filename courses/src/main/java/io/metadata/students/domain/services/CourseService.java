package io.metadata.students.domain.services;

import io.metadata.api.courses.CourseResponse;
import io.metadata.students.domain.model.Course;
import io.metadata.students.domain.ports.input.CreateCourseUseCase;
import io.metadata.students.domain.ports.input.DeleteCourseUseCase;
import io.metadata.students.domain.ports.input.FetchCourseUseCase;
import io.metadata.students.domain.ports.input.GetCourseUseCase;
import io.metadata.students.domain.ports.input.UpdateCourseStateUseCase;
import io.metadata.students.domain.ports.input.UpdateCourseUseCase;
import io.metadata.students.domain.ports.output.CourseMessageSender;
import io.metadata.students.domain.ports.output.CourseOutputPort;
import io.metadata.students.domain.services.mapper.ServiceMapper;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    public Long create(final CreateCourseUseCase.Command command)
    {
        val name = mapper.commandToCourseName(command);
        val course = courseOutputPort.save(name);
        val createdEvent = mapper.courseToMessage(course);
        courseEventPublisher.publishMessageForCreated(createdEvent);
        return course.getId().getValue();
    }

    @Override
    @CacheEvict(value = "course", key = "{#command.id}")
    @Transactional
    public void delete(final DeleteCourseUseCase.Command command)
    {
        val id = mapper.commandToCourseId(command);
        courseOutputPort.deleteById(id);
        val deletedEvent = mapper.courseIdToMessage(id);

        courseEventPublisher.publishMessageForDeleted(deletedEvent);
    }

    @Override
    public Collection<CourseResponse> fetch()
    {
        return courseOutputPort.fetch().stream()
            .map(mapper::domainToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "course", key = "{#id }")
    public CourseResponse getById(final Long id)
    {
        val course = courseOutputPort.getById(mapper.mapId(id));
        return mapper.domainToResponse(course);
    }

    @Override
    @Cacheable(value = "course", key = "{#command.id}")
    public CourseResponse update(final UpdateCourseUseCase.Command command)
    {
        final Course course = mapper.commandToDomain(command);
        courseOutputPort.update(course);
        return mapper.domainToResponse(course);
    }

    @Override
    public Long updateState(final UpdateCourseStateUseCase.Command command)
    {
        val idValue = command.getId();
        val id = mapper.mapId(idValue);
        courseOutputPort.updateState(id, command.getState());
        return idValue;
    }
}
