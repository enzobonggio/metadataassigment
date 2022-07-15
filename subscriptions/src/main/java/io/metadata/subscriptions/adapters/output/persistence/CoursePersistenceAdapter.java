package io.metadata.subscriptions.adapters.output.persistence;

import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.subscriptions.adapters.output.persistence.repository.CourseRepository;
import io.metadata.subscriptions.adapters.output.persistence.repository.StudentRepository;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort;
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseOutputPort
{
    private final CourseRepository courseRepository;
    private final PersistenceMapper mapper;

    @Override
    public CourseId save(final CourseId id)
    {
        val studentEntity = courseRepository
            .save(mapper.domainToEntity(id));
        return mapper.entityToDomain(studentEntity);
    }
}
