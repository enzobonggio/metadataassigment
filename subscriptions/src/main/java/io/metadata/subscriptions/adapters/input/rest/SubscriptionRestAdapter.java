package io.metadata.subscriptions.adapters.input.rest;

import io.metadata.api.courses.CourseResponse;
import io.metadata.api.students.StudentResponse;
import io.metadata.api.subscriptions.CourseFilterState;
import io.metadata.api.subscriptions.StudentFilterState;
import io.metadata.api.subscriptions.SubscriptionRequest;
import io.metadata.api.subscriptions.SubscriptionResponse;
import io.metadata.subscriptions.adapters.input.mapper.RestMapper;
import io.metadata.subscriptions.domain.ports.input.FetchEmptyCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchLazyStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase;
import io.metadata.subscriptions.domain.services.exception.CourseNotFoundException;
import io.metadata.subscriptions.domain.services.exception.StudentNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionRestAdapter
{
    private final FetchSubscriptionsByCourseUseCase fetchSubscriptionsByCourseUseCase;
    private final FetchSubscriptionsByStudentUseCase fetchSubscriptionsByStudentUseCase;
    private final FetchLazyStudentUseCase fetchLazyStudentUseCase;
    private final FetchEmptyCourseUseCase fetchEmptyCourseUseCase;
    private final SubscribeToCourseUseCase subscribeToCourseUseCase;
    private final RestMapper mapper;

    @Operation(summary = "Subscribe a course with a student")
    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@RequestBody SubscriptionRequest request)
    {
        val command = mapper.requestToSubscribeToCourseCommand(request);
        log.trace("course create command {}", command);
        val response = subscribeToCourseUseCase.subscribe(command);
        log.trace("course create response {}", response);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @Operation(summary = "Fetch subscription courses by filters")
    @GetMapping("/courses")
    public ResponseEntity<Collection<CourseResponse>> fetch(
        @Parameter(description = "id of the student to filter") @RequestParam(value = "studentId", required = false) Long studentId,
        @Parameter(description = "state to filter", example = "EMPTY") @RequestParam(value = "state", required = false) CourseFilterState state)
    {
        log.trace("fetch params studentId {} state {}", studentId, state);

        // both are null or both are not null
        if ((state == null) == (studentId == null)) {
            return ResponseEntity.badRequest().build();
        }

        if (state != null) {
            if (state == CourseFilterState.EMPTY) {
                val emptyCourses = fetchEmptyCourseUseCase.fetchEmptyCourse();
                log.trace("fetch empty courses response {}", emptyCourses);
                return ResponseEntity.ok(emptyCourses);
            }
        }

        val coursesByStudentId = fetchSubscriptionsByStudentUseCase.fetchByStudentId(studentId);
        log.trace("fetch courses by studentId {} response {}", studentId, coursesByStudentId);
        return ResponseEntity.ok(coursesByStudentId);
    }

    @Operation(summary = "Fetch subscription students by filters")
    @GetMapping("/students")
    public ResponseEntity<Collection<StudentResponse>> fetch(
        @Parameter(description = "id of the course to filter") @RequestParam(value = "courseId", required = false) Long courseId,
        @Parameter(description = "state to filter", example = "UNASSIGNED") @RequestParam(value = "state", required = false) StudentFilterState state)
    {
        // both are null or both are not null
        if ((state == null) == (courseId == null)) {
            return ResponseEntity.badRequest().build();
        }

        if (state == StudentFilterState.UNASSIGNED) {
            val unassignedStudents = fetchLazyStudentUseCase.fetchLazyStudent();
            log.trace("fetch unassigned students response {}", unassignedStudents);
            return ResponseEntity.ok(unassignedStudents);
        }

        val studentsByCourseId = fetchSubscriptionsByCourseUseCase.fetchByCourseId(courseId);
        log.trace("fetch students by courseId {} response {}", courseId, studentsByCourseId);
        return ResponseEntity.ok(studentsByCourseId);
    }
}
