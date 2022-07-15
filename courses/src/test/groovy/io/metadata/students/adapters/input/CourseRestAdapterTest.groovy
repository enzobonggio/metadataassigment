package io.metadata.students.adapters.input

import io.github.joke.spockmockable.Mockable
import io.metadata.api.courses.CourseRequest
import io.metadata.api.courses.CourseResponse
import io.metadata.students.adapters.input.rest.CourseRestAdapter
import io.metadata.students.adapters.mapper.AdapterMapper
import io.metadata.students.domain.ports.input.*
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

@Mockable([
        CourseResponse,
        CreateCourseUseCase.Command,
        DeleteCourseUseCase.Command,
        UpdateCourseUseCase.Command
])
class CourseRestAdapterTest extends Specification {

    def getCourseUseCase = Mock(GetCourseUseCase)
    def createCourseUseCase = Mock(CreateCourseUseCase)
    def updateCourseUseCase = Mock(UpdateCourseUseCase)
    def fetchCourseUseCase = Mock(FetchCourseUseCase)
    def deleteCourseUseCase = Mock(DeleteCourseUseCase)
    def mapper = Mock(AdapterMapper)

    def courseRestAdapter = new CourseRestAdapter(
            getCourseUseCase,
            createCourseUseCase,
            updateCourseUseCase,
            fetchCourseUseCase,
            deleteCourseUseCase,
            mapper
    )

    def "should get"() {
        given:
        def expectedCourse = Mock(CourseResponse)
        def courseId = 10L
        when:
        def courseResponse = courseRestAdapter.get(courseId)
        then:
        1 * getCourseUseCase.getById(courseId) >> expectedCourse
        courseResponse.body == expectedCourse
    }

    def "should fetch"() {
        given:
        def expectedCourses = Mock(List<CourseResponse>)
        when:
        def courseResponse = courseRestAdapter.fetch()
        then:
        1 * fetchCourseUseCase.fetch() >> expectedCourses
        courseResponse.body == expectedCourses
    }

    def "should create"() {
        given:
        def courseRequest = Mock(CourseRequest)
        def builder = Mock(UriComponentsBuilder)
        def uriComponents = Mock(UriComponents)
        def uri = URI.create("")
        def command = Mock(CreateCourseUseCase.Command)
        def courseId = 10L
        when:
        def courseResponse = courseRestAdapter.create(courseRequest, builder)
        then:
        1 * mapper.requestToCreateCommand(courseRequest) >> command
        1 * createCourseUseCase.create(command) >> courseId
        1 * builder.replacePath("/courses/{id}") >> builder
        1 * builder.buildAndExpand(courseId) >> uriComponents
        1 * uriComponents.toUri() >> uri
        courseResponse.headers.getLocation() == uri
        courseResponse.statusCode == HttpStatus.CREATED
    }

    def "should delete"() {
        given:
        def command = Mock(DeleteCourseUseCase.Command)
        def courseId = 10L
        when:
        def courseResponse = courseRestAdapter.delete(courseId)
        then:
        1 * mapper.requestToDeleteCommand(courseId) >> command
        1 * deleteCourseUseCase.delete(command) >> {}
        courseResponse.statusCode == HttpStatus.NO_CONTENT
    }

    def "should update"() {
        given:
        def courseRequest = Mock(CourseRequest)
        def command = Mock(UpdateCourseUseCase.Command)
        def courseId = 10L
        def expectedCourseResponse = Mock(CourseResponse)
        when:
        def courseResponse = courseRestAdapter.update(courseId, courseRequest)
        then:
        1 * mapper.requestToUpdateCommand(courseId, courseRequest) >> command
        1 * updateCourseUseCase.update(command) >> expectedCourseResponse
        courseResponse.body == expectedCourseResponse
    }
}
