package io.metadata.students.adapters.output

import io.github.joke.spockmockable.Mockable
import io.metadata.api.commons.State
import io.metadata.students.adapters.output.persistence.StudentPersistenceAdapter
import io.metadata.students.adapters.output.persistence.entity.StudentEntity
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper
import io.metadata.students.adapters.output.persistence.repository.StudentRepository
import io.metadata.students.domain.model.Student
import io.metadata.students.domain.model.StudentId
import io.metadata.students.domain.model.StudentName
import spock.lang.Specification

@Mockable([
        StudentName,
        StudentEntity,
        Student,
        StudentId
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

    def "should update student"() {
        given:
        def studentEntity = Mock(StudentEntity)
        def studentToUpdate = Mock(Student)
        def studentIdValue = 10L
        def studentNameValue = "name"
        when:
        studentPersistenceAdapter.update(studentToUpdate)
        then:
        1 * mapper.domainToEntity(studentToUpdate) >> studentEntity
        1 * studentEntity.getId() >> studentIdValue
        1 * studentEntity.getName() >> studentNameValue
        1 * studentRepository.update(studentIdValue, studentNameValue) >> studentIdValue
    }

    def "should update state student"() {
        given:
        def studentIdToUpdate = Mock(StudentId)
        def studentStateToUpdate = State.CREATED
        def studentIdValue = 10L
        when:
        studentPersistenceAdapter.updateState(studentIdToUpdate, studentStateToUpdate)
        then:
        1 * studentIdToUpdate.getValue() >> studentIdValue
        1 * studentRepository.updateState(studentIdValue, studentStateToUpdate.toString()) >> studentIdValue
    }

    def "should delete student"() {
        given:
        def studentIdToDelete = Mock(StudentId)
        def studentIdValue = 10L
        when:
        studentPersistenceAdapter.deleteById(studentIdToDelete)
        then:
        1 * studentIdToDelete.getValue() >> studentIdValue
        1 * studentRepository.deleteById(studentIdValue) >> {}
    }

    def "should fetch students"() {
        given:
        def studentEntity = Mock(StudentEntity)
        def studentEntities = [studentEntity]
        def student = Mock(Student)
        def expectedStudents = [student]
        when:
        def students = studentPersistenceAdapter.fetch()
        then:
        1 * studentRepository.findAll() >> studentEntities
        1 * mapper.entityToDomain(studentEntity) >> student
        students == expectedStudents
    }
}
