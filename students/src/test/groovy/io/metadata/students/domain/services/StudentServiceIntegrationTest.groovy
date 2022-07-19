package io.metadata.students.domain.services

import io.metadata.api.commons.State
import io.metadata.students.adapters.config.AdapterTestConfiguration
import io.metadata.students.adapters.output.InMemoryStudentPersistenceAdapter
import io.metadata.students.adapters.output.persistence.exception.StudentNotFoundException
import io.metadata.students.domain.ports.input.CreateStudentUseCase
import io.metadata.students.domain.ports.input.DeleteStudentUseCase
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase
import io.metadata.students.domain.ports.input.UpdateStudentUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@Import([AdapterTestConfiguration])
@ActiveProfiles(["kafka-less", "test"])
class StudentServiceIntegrationTest extends Specification {
    @Autowired
    StudentService studentService

    @Autowired
    InMemoryStudentPersistenceAdapter studentAdapter;

    void cleanup() {
        studentAdapter.clear()
    }

    def "get created student"() {
        given:
        def studentId = createStudent("name")
        when:
        def student = studentService.getById(studentId)
        then:
        studentId == student.id
        student.name == "name"
        student.state == State.CREATING

    }

    def "get updated student"() {
        given:
        def studentId = createStudent("name")
        def command = UpdateStudentUseCase.Command.builder()
                .name("name2")
                .id(studentId)
                .build()
        when:
        studentService.update(command)
        def student = studentService.getById(studentId)
        then:
        studentId == student.id
        student.name == "name2"
        student.state == State.CREATING
    }

    def "get delete student"() {
        given:
        def studentId = createStudent("name")
        def command = DeleteStudentUseCase.Command.builder()
                .id(studentId)
                .build()
        when:
        studentService.delete(command)
        def student = studentService.getById(studentId)
        then:
        thrown StudentNotFoundException
    }

    def "fetch created student"() {
        given:
        def studentId1 = createStudent("name1")
        def studentId2 = createStudent("name2")
        when:
        def studentResponses = studentService.fetch()
        then:
        studentResponses.collect { it.name }.toSet() == ["name1", "name2"].toSet()
        studentResponses.collect { it.id }.toSet() == [studentId1, studentId2].toSet()
    }

    def "get updated state student"() {
        given:
        def studentId = createStudent("name")
        def command = UpdateStudentStateUseCase.Command.builder()
                .id(studentId)
                .state(State.CREATED)
                .build()
        when:
        studentService.updateState(command)
        def student = studentService.getById(studentId)
        then:
        studentId == student.id
        student.name == "name"
        student.state == State.CREATED
    }

    def createStudent(String name) {
        def command = CreateStudentUseCase.Command.builder()
                .name(name)
                .build()
        return studentService.create(command)
    }

}
