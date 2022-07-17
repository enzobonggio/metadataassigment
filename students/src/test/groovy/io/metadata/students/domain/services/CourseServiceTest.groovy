package io.metadata.students.domain.services

import io.github.joke.spockmockable.Mockable
import io.metadata.api.Students
import io.metadata.api.commons.State
import io.metadata.api.students.StudentResponse
import io.metadata.students.domain.model.Student
import io.metadata.students.domain.model.StudentId
import io.metadata.students.domain.model.StudentName
import io.metadata.students.domain.ports.input.CreateStudentUseCase
import io.metadata.students.domain.ports.input.DeleteStudentUseCase
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase
import io.metadata.students.domain.ports.input.UpdateStudentUseCase
import io.metadata.students.domain.ports.output.StudentMessageSender
import io.metadata.students.domain.ports.output.StudentOutputPort
import io.metadata.students.domain.services.mapper.ServiceMapper
import spock.lang.Specification

@Mockable([
        StudentResponse,
        StudentName,
        StudentId,
        Student,
        DeleteStudentUseCase.Command,
        UpdateStudentUseCase.Command,
        CreateStudentUseCase.Command,
        UpdateStudentStateUseCase.Command,
        Students.StudentMessage])
class StudentServiceTest extends Specification {
    def studentOutputPort = Mock(StudentOutputPort)
    def studentEventPublisher = Mock(StudentMessageSender)
    def mapper = Mock(ServiceMapper)
    def studentService = Spy(new StudentService(studentOutputPort, studentEventPublisher, mapper))

    def "should create student"() {
        given:
        def studentName = Mock(StudentName)
        def studentId = Mock(StudentId)
        def student = Mock(Student)
        def studentMessage = Mock(Students.StudentMessage)
        def command = Mock(CreateStudentUseCase.Command)
        def expectedStudentIdValue = 10L
        when:
        def studentIdValue = studentService.create(command)
        then:
        1 * mapper.commandToStudentName(command) >> studentName
        1 * studentOutputPort.save(studentName) >> student
        1 * mapper.studentToMessage(student) >> studentMessage
        1 * studentEventPublisher.publishMessageForCreated(studentMessage) >> {}
        1 * student.getId() >> studentId
        1 * studentId.getValue() >> expectedStudentIdValue
        studentIdValue == expectedStudentIdValue
    }

    def "should update student"() {
        given:
        def studentToUpdate = Mock(Student)
        def command = Mock(UpdateStudentUseCase.Command)
        def id = 10L
        def expectedStudentResponse = Mock(StudentResponse)
        when:
        def studentResponse = studentService.update(command)
        then:
        1 * mapper.commandToDomain(command) >> studentToUpdate
        1 * studentOutputPort.update(studentToUpdate) >> {}
        1 * command.getId() >> id
        1 * studentService.getById(id) >> expectedStudentResponse
        studentResponse == expectedStudentResponse
    }

    def "should delete student"() {
        given:
        def studentId = Mock(StudentId)
        def command = Mock(DeleteStudentUseCase.Command)
        def studentMessage = Mock(Students.StudentMessage)
        when:
        studentService.delete(command)
        then:
        1 * mapper.commandToStudentId(command) >> studentId
        1 * mapper.studentIdToMessage(studentId) >> studentMessage
        1 * studentOutputPort.deleteById(studentId) >> {}
        1 * studentEventPublisher.publishMessageForDeleted(studentMessage) >> {}
    }

    def "should get student by id"() {
        given:
        def studentIdValue = 10L
        def studentId = Mock(StudentId)
        def student = Mock(Student)
        def expectedStudentResponse = Mock(StudentResponse)
        when:
        def studentResponse = studentService.getById(studentIdValue)
        then:
        1 * mapper.mapId(studentIdValue) >> studentId
        1 * studentOutputPort.getById(studentId) >> student
        1 * mapper.domainToResponse(student) >> expectedStudentResponse
        studentResponse == expectedStudentResponse
    }

    def "should fetch students"() {
        given:
        def student = Mock(Student)
        def expectedStudentResponse = Mock(StudentResponse)
        when:
        def studentResponses = studentService.fetch()
        then:
        1 * studentOutputPort.fetch() >> [student]
        1 * mapper.domainToResponse(student) >> expectedStudentResponse
        studentResponses == [expectedStudentResponse]
    }


    def "should update state"() {
        given:
        def command = Mock(UpdateStudentStateUseCase.Command)
        def studentId = Mock(StudentId)
        def studentIdValue = 10L
        def expectedStudentResponse = Mock(StudentResponse)
        when:
        def studentResponse = studentService.updateState(command)
        then:
        1 * command.getId() >> studentIdValue
        1 * command.getState() >> State.CREATING
        1 * mapper.mapId(studentIdValue) >> studentId
        1 * studentOutputPort.updateState(studentId, State.CREATING) >> {}
        1 * studentService.getById(studentIdValue) >> expectedStudentResponse
        studentResponse == expectedStudentResponse
    }
}
