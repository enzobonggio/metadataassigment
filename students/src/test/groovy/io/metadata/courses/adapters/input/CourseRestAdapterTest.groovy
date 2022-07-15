package io.metadata.students.adapters.input

import io.github.joke.spockmockable.Mockable
import io.metadata.api.students.StudentRequest
import io.metadata.api.students.StudentResponse
import io.metadata.students.adapters.input.rest.StudentRestAdapter
import io.metadata.students.adapters.mapper.AdapterMapper
import io.metadata.students.domain.ports.input.*
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

@Mockable([
        StudentResponse,
        CreateStudentUseCase.Command,
        DeleteStudentUseCase.Command,
        UpdateStudentUseCase.Command
])
class StudentRestAdapterTest extends Specification {

    def getStudentUseCase = Mock(GetStudentUseCase)
    def createStudentUseCase = Mock(CreateStudentUseCase)
    def updateStudentUseCase = Mock(UpdateStudentUseCase)
    def fetchStudentUseCase = Mock(FetchStudentUseCase)
    def deleteStudentUseCase = Mock(DeleteStudentUseCase)
    def mapper = Mock(AdapterMapper)

    def studentRestAdapter = new StudentRestAdapter(
            getStudentUseCase,
            createStudentUseCase,
            updateStudentUseCase,
            fetchStudentUseCase,
            deleteStudentUseCase,
            mapper
    )

    def "should get"() {
        given:
        def expectedStudent = Mock(StudentResponse)
        def studentId = 10L
        when:
        def studentResponse = studentRestAdapter.get(studentId)
        then:
        1 * getStudentUseCase.getById(studentId) >> expectedStudent
        studentResponse.body == expectedStudent
    }

    def "should fetch"() {
        given:
        def expectedStudents = Mock(List<StudentResponse>)
        when:
        def studentResponse = studentRestAdapter.fetch()
        then:
        1 * fetchStudentUseCase.fetch() >> expectedStudents
        studentResponse.body == expectedStudents
    }

    def "should create"() {
        given:
        def studentRequest = Mock(StudentRequest)
        def builder = Mock(UriComponentsBuilder)
        def uriComponents = Mock(UriComponents)
        def uri = URI.create("")
        def command = Mock(CreateStudentUseCase.Command)
        def studentId = 10L
        when:
        def studentResponse = studentRestAdapter.create(studentRequest, builder)
        then:
        1 * mapper.requestToCreateCommand(studentRequest) >> command
        1 * createStudentUseCase.create(command) >> studentId
        1 * builder.replacePath("/students/{id}") >> builder
        1 * builder.buildAndExpand(studentId) >> uriComponents
        1 * uriComponents.toUri() >> uri
        studentResponse.headers.getLocation() == uri
        studentResponse.statusCode == HttpStatus.CREATED
    }

    def "should delete"() {
        given:
        def command = Mock(DeleteStudentUseCase.Command)
        def studentId = 10L
        when:
        def studentResponse = studentRestAdapter.delete(studentId)
        then:
        1 * mapper.requestToDeleteCommand(studentId) >> command
        1 * deleteStudentUseCase.delete(command) >> {}
        studentResponse.statusCode == HttpStatus.NO_CONTENT
    }

    def "should update"() {
        given:
        def studentRequest = Mock(StudentRequest)
        def command = Mock(UpdateStudentUseCase.Command)
        def studentId = 10L
        def expectedStudentResponse = Mock(StudentResponse)
        when:
        def studentResponse = studentRestAdapter.update(studentId, studentRequest)
        then:
        1 * mapper.requestToUpdateCommand(studentId, studentRequest) >> command
        1 * updateStudentUseCase.update(command) >> expectedStudentResponse
        studentResponse.body == expectedStudentResponse
    }
}
