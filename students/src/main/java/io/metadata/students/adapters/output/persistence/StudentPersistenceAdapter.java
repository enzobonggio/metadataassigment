package io.metadata.students.adapters.output.persistence;

import io.metadata.api.commons.State;
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.students.adapters.output.persistence.repository.StudentRepository;
import io.metadata.students.domain.model.Student;
import io.metadata.students.domain.model.StudentId;
import io.metadata.students.domain.model.StudentName;
import io.metadata.students.domain.ports.output.StudentOutputPort;
import io.metadata.students.adapters.output.persistence.exception.StudentNotFoundException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class StudentPersistenceAdapter implements StudentOutputPort
{
    private final StudentRepository studentRepository;
    private final PersistenceMapper mapper;

    @Override
    public Student save(final StudentName name)
    {
        val studentEntity = studentRepository
            .save(mapper.domainToEntity(name));
        return mapper.entityToDomain(studentEntity);
    }

    @Override
    @CacheEvict(value = "student", key = "{#student.id.value}")
    public void update(final Student student)
    {

        val entity = mapper.domainToEntity(student);
        studentRepository
            .update(entity.getId(), entity.getName());
    }

    @Override
    public void updateState(final StudentId studentId, State state)
    {
        studentRepository
            .updateState(studentId.getValue(), state.toString());
    }

    @Override
    @Cacheable(value = "student", key = "{#id.value}")
    public Student getById(final StudentId id)
    {
        return studentRepository.findById(id.getValue())
            .map(mapper::entityToDomain)
            .orElseThrow(() -> new StudentNotFoundException(id.getValue()));
    }

    @Override
    @CacheEvict(value = "student", key = "{#id.value}")
    public void deleteById(final StudentId id)
    {
        studentRepository.deleteById(id.getValue());
    }

    @Override
    public Collection<Student> fetch()
    {
        val students = studentRepository.findAll();

        return StreamSupport.stream(students.spliterator(), false)
            .map(mapper::entityToDomain)
            .collect(Collectors.toList());
    }
}
