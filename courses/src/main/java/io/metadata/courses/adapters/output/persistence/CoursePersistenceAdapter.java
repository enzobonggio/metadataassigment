package io.metadata.courses.adapters.output.persistence;

import io.metadata.api.commons.State;
import io.metadata.courses.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.courses.adapters.output.persistence.repository.CourseRepository;
import io.metadata.courses.domain.model.Course;
import io.metadata.courses.domain.model.CourseId;
import io.metadata.courses.domain.model.CourseName;
import io.metadata.courses.domain.ports.output.CourseOutputPort;
import io.metadata.courses.adapters.output.persistence.exception.CourseNotFoundException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseOutputPort
{
    private final CourseRepository courseRepository;
    private final PersistenceMapper mapper;

    @Override
    public Course save(final CourseName name)
    {
        val courseEntity = courseRepository
            .save(mapper.domainToEntity(name));
        return mapper.entityToDomain(courseEntity);
    }

    @Override
    @CacheEvict(value = "course", key = "{#course.id.value}")
    public void update(final Course course)
    {

        val entity = mapper.domainToEntity(course);
        courseRepository
            .update(entity.getId(), entity.getName());
    }

    @Override
    public void updateState(final CourseId courseId, State state)
    {
        courseRepository
            .updateState(courseId.getValue(), state.toString());
    }

    @Override
    @Cacheable(value = "course", key = "{#id.value}")
    public Course getById(final CourseId id)
    {
        return courseRepository.findById(id.getValue())
            .map(mapper::entityToDomain)
            .orElseThrow(() -> new CourseNotFoundException(id.getValue()));
    }

    @Override
    @CacheEvict(value = "course", key = "{#id.value}")
    public void deleteById(final CourseId id)
    {
        courseRepository.deleteById(id.getValue());
    }

    @Override
    public Collection<Course> fetch()
    {
        val courses = courseRepository.findAll();

        return StreamSupport.stream(courses.spliterator(), false)
            .map(mapper::entityToDomain)
            .collect(Collectors.toList());
    }
}
