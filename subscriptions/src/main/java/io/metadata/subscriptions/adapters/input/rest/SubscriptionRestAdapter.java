package io.metadata.subscriptions.adapters.input.rest;

import io.metadata.api.subscriptions.SubscriptionRequest;
import io.metadata.api.subscriptions.SubscriptionResponse;
import io.metadata.api.subscriptions.SubscriptionsResponse;
import io.metadata.subscriptions.adapters.input.mapper.RestMapper;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("subscriptions")
@RequiredArgsConstructor
public class SubscriptionRestAdapter
{
    private final FetchSubscriptionsByCourseUseCase fetchSubscriptionsByCourseUseCase;
    private final FetchSubscriptionsByStudentUseCase fetchSubscriptionsByStudentUseCase;
    private final SubscribeToCourseUseCase subscribeToCourseUseCase;
    private final RestMapper mapper;

    @GetMapping
    public ResponseEntity<SubscriptionsResponse> fetch(
        @RequestParam(value = "courseId", required = false) Long courseId,
        @RequestParam(value = "studentId", required = false) Long studentId)
    {
        // both are null or both are not null
        if ((courseId == null) == (studentId == null)) {
            return ResponseEntity.badRequest().build();
        }

        if (courseId != null) {
            return ResponseEntity.ok(fetchSubscriptionsByCourseUseCase.getByCourseId(courseId));
        }
        return ResponseEntity.ok(fetchSubscriptionsByStudentUseCase.getByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@RequestBody SubscriptionRequest request, UriComponentsBuilder builder)
    {
        val command = mapper.requestToSubscribeToCourseCommand(request);
        val response = subscribeToCourseUseCase.subscribe(command);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }
}
