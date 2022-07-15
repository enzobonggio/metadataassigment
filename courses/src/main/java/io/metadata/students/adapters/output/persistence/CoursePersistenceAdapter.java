package io.metadata.students.adapters.output.persistence;

import io.metadata.api.commons.State;
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.students.adapters.output.persistence.repository.CourseRepository;
import io.metadata.students.domain.model.Course;
import io.metadata.students.domain.model.CourseId;
import io.metadata.students.domain.model.CourseName;
import io.metadata.students.domain.ports.output.CourseOutputPort;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;

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
    public void update(final Course course)
    {

        val entity = mapper.domainToEntity(course);
        courseRepository
            .save(entity);
    }

    @Override
    public void updateState(final CourseId courseId, State state)
    {
        courseRepository
            .updateState(courseId.getValue(), state.toString());
    }

    @Override
    public Course getById(final CourseId id)
    {
        return courseRepository.findById(id.getValue())
            .map(mapper::entityToDomain)
            .orElseThrow(RuntimeException::new);
    }

    @Override
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
