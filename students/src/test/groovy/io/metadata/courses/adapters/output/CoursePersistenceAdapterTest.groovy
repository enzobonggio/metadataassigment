package io.metadata.students.adapters.output

import io.github.joke.spockmockable.Mockable
import io.metadata.students.adapters.output.persistence.StudentPersistenceAdapter
import io.metadata.students.adapters.output.persistence.entity.StudentEntity
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.students.adapters.output.persistence.repository.StudentRepository
import io.metadata.students.domain.model.Student
import io.metadata.students.domain.model.StudentName
import spock.lang.Specification

@Mockable([
        StudentName,
        StudentEntity,
        Student
])
class StudentPersistenceAdapterTest extends Specification {

    def studentRepository = Mock(StudentRepository)
    def mapper = Mock(PersistenceMapper)
    def studentPersistenceAdapter = new StudentPersistenceAdapter(studentRepository, mapper)

    def "should save student"() {
        given:
        def studentName = Mock(StudentName)
        def studentEntity = Mock(StudentEntity)
        def studentDomain = Mock(Student)
        when:
        def student = studentPersistenceAdapter.save(studentName)
        then:
        1 * mapper.domainToEntity(studentName) >> studentEntity
        1 * mapper.entityToDomain(studentEntity) >> studentDomain
        1 * studentRepository.save(studentEntity) >> studentEntity
        student == studentDomain
    }
}
