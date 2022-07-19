package io.metadata.subscriptions.adapters.input.rest;

import io.metadata.api.subscriptions.CourseFilterState;
import io.metadata.api.subscriptions.CourseResponse;
import io.metadata.subscriptions.domain.ports.input.FetchEmptyCourseUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
public class CourseRestAdapter
{
    private final FetchEmptyCourseUseCase fetchEmptyCourseUseCase;

    @Operation(summary = "Fetch courses by filters")
    @GetMapping
    public ResponseEntity<Collection<CourseResponse>> get(
        @Parameter(description = "state to filter", example = "EMPTY", required = true) @RequestParam("state") CourseFilterState state)
    {
        if (state == CourseFilterState.EMPTY) {
            return ResponseEntity.ok(fetchEmptyCourseUseCase.fetchEmptyCourse());
        }

        throw new NotImplementedException("Implementation for state " + state + " is not implemented yet");
    }
}
