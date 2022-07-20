package io.metadata.subscriptions.adapters.output.persistence;

import io.metadata.subscriptions.adapters.output.persistence.exception.DuplicatedCourseException;
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.subscriptions.adapters.output.persistence.repository.CourseRepository;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseOutputPort
{
    private final CourseRepository courseRepository;
    private final PersistenceMapper mapper;

    @Override
    public CourseId save(final CourseId id)
    {
        try {
            val courseEntity = courseRepository
                .save(mapper.domainToEntity(id));
            return mapper.entityToDomain(courseEntity);
        }
        catch (DbActionExecutionException e) {
            if (e.getCause() instanceof DuplicateKeyException) {
                throw new DuplicatedCourseException(id);
            }
            throw e;
        }
    }

    @Override
    @Cacheable(value = "course-exists", key = "{#id.value}")
    public Boolean exists(CourseId id)
    {
        return courseRepository.existsById(id.getValue());
    }

    @Override
    @CacheEvict(value = {"course", "course-exists"}, key = "{#id.value}")
    public CourseId delete(final CourseId id)
    {
        courseRepository.softDeleteById(id.getValue());
        return id;
    }

    @Override
    public Collection<CourseId> fetchEmpty()
    {
        val courses = courseRepository.findAllEmpty();
        return StreamSupport.stream(courses.spliterator(), false)
            .map(mapper::entityToDomain)
            .collect(Collectors.toList());
    }
}
