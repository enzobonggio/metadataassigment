package io.metadata.courses.adapters.input.rest;

import io.metadata.api.courses.CourseRequest;
import io.metadata.api.courses.CourseResponse;
import io.metadata.courses.adapters.input.rest.mapper.RestMapper;
import io.metadata.courses.domain.ports.input.CreateCourseUseCase;
import io.metadata.courses.domain.ports.input.DeleteCourseUseCase;
import io.metadata.courses.domain.ports.input.FetchCourseUseCase;
import io.metadata.courses.domain.ports.input.GetCourseUseCase;
import io.metadata.courses.domain.ports.input.UpdateCourseUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
public class CourseRestAdapter
{
    private final GetCourseUseCase getCourseUseCase;
    private final CreateCourseUseCase createCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;
    private final FetchCourseUseCase fetchCourseUseCase;
    private final DeleteCourseUseCase deleteCourseUseCase;
    private final RestMapper mapper;

    @Operation(summary = "Get a course by its id")
    @GetMapping(value = "{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponse> get(
        @Parameter(description = "id of the course", required = true) @PathVariable("courseId") long courseId)
    {
        val course = getCourseUseCase.getById(courseId);
        return ResponseEntity.ok(course);
    }

    @Operation(summary = "Fetch courses")
    @GetMapping
    public ResponseEntity<Collection<CourseResponse>> fetch()
    {
        val courses = fetchCourseUseCase.fetch();
        return ResponseEntity.ok(courses);
    }

    @Operation(summary = "Create a course")
    @PostMapping
    public ResponseEntity<Void> create(
        @RequestBody CourseRequest request, UriComponentsBuilder builder)
    {
        val command = mapper.requestToCreateCommand(request);
        val id = createCourseUseCase.create(command);
        val location = builder.replacePath("/courses/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Modify a course")
    @PutMapping("{courseId}")
    public ResponseEntity<CourseResponse> update(
        @Parameter(description = "id of the course", required = true) @PathVariable("courseId") long courseId,
        @RequestBody CourseRequest request)
    {
        val command = mapper.requestToUpdateCommand(courseId, request);

        val course = updateCourseUseCase.update(command);
        return ResponseEntity.ok(course);
    }

    @Operation(summary = "Delete a course by its id")
    @DeleteMapping("{courseId}")
    public ResponseEntity<Void> delete(@PathVariable("courseId") long courseId)
    {
        val command = mapper.requestToDeleteCommand(courseId);

        deleteCourseUseCase.delete(command);
        return ResponseEntity.noContent().build();
    }
}
