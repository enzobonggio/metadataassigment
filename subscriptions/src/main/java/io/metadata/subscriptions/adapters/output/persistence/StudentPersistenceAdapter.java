package io.metadata.subscriptions.adapters.output.persistence;

import io.metadata.subscriptions.adapters.output.persistence.exception.DuplicatedStudentException;
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.subscriptions.adapters.output.persistence.repository.StudentRepository;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

@RequiredArgsConstructor
public class StudentPersistenceAdapter implements StudentOutputPort
{
    private final StudentRepository studentRepository;
    private final PersistenceMapper mapper;

    @Override
    public StudentId save(final StudentId id)
    {
        try {
            val studentEntity = studentRepository
                .save(mapper.domainToEntity(id));
            return mapper.entityToDomain(studentEntity);
        }
        catch (DbActionExecutionException e) {
            if (e.getCause() instanceof DuplicateKeyException) {
                throw new DuplicatedStudentException(id);
            }
            throw e;
        }
    }

    @Override
    @CacheEvict(value = "student", key = "{#id.value}")
    public StudentId delete(final StudentId id)
    {
        studentRepository.deleteById(id.getValue());
        return id;
    }

    @Override
    public Collection<StudentId> fetchLazy()
    {
        val students = studentRepository.findAllLazy();
        return StreamSupport.stream(students.spliterator(), false)
            .map(mapper::entityToDomain)
            .collect(Collectors.toList());
    }
}
