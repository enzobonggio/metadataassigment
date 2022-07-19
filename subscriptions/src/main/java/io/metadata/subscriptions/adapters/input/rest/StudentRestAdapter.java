package io.metadata.subscriptions.adapters.input.rest;

import io.metadata.api.subscriptions.StudentFilterState;
import io.metadata.api.subscriptions.StudentResponse;
import io.metadata.subscriptions.domain.ports.input.FetchLazyStudentUseCase;
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
@RequestMapping("students")
@RequiredArgsConstructor
public class StudentRestAdapter
{
    private final FetchLazyStudentUseCase fetchLazyStudentUseCase;

    @Operation(summary = "Fetch students by filters")
    @GetMapping
    public ResponseEntity<Collection<StudentResponse>> get(
        @Parameter(description = "state to filter", example = "EMPTY", required = true) @RequestParam("state") StudentFilterState state)
    {
        if (state == StudentFilterState.UNASSIGNED) {
            return ResponseEntity.ok(fetchLazyStudentUseCase.fetchLazyStudent());
        }

        throw new NotImplementedException("Implementation for state " + state + " is not implemented yet");
    }
}
