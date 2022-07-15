package io.metadata.subscriptions.domain.services;

import io.metadata.api.students.StudentResponse;
import io.metadata.api.subscriptions.CourseResponse;
import io.metadata.api.subscriptions.SubscriptionResponse;
import io.metadata.api.subscriptions.SubscriptionsResponse;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.Subscription;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase;
import io.metadata.subscriptions.domain.ports.output.CourseServicePort;
import io.metadata.subscriptions.domain.ports.output.StudentServicePort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort;
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class SubscriptionsByStudentService implements
    SubscribeToCourseUseCase,
    FetchSubscriptionsByCourseUseCase,
    FetchSubscriptionsByStudentUseCase
{
    final SubscriptionOutputPort subscriptionOutputPort;
    final StudentServicePort studentServicePort;
    final CourseServicePort courseServicePort;
    final ServiceMapper mapper;

    @Override
    public SubscriptionsResponse getByStudentId(final Long studentId)
    {
        val id = mapper.mapStudentId(studentId);
        return getBy(subscriptionOutputPort.fetchByStudentId(id));
    }

    @Override
    public SubscriptionsResponse getByCourseId(final Long courseId)
    {
        val id = mapper.mapCourseId(courseId);
        return getBy(subscriptionOutputPort.fetchByCourseId(id));
    }

    @Override
    public SubscriptionResponse subscribe(final Command command)
    {
        val subscription = mapper.commandToDomain(command);
        val savedSubscription = subscriptionOutputPort.save(subscription);
        return mapper.domainToResponse(savedSubscription);
    }

    public SubscriptionsResponse getBy(final Collection<Subscription> subscriptions)
    {
        val courses = subscriptions.stream()
            .map(Subscription::getCourseId)
            .distinct()
            .map(courseServicePort::getById)
            .collect(Collectors.toMap(io.metadata.api.courses.CourseResponse::getId, Function.identity()));
        val students = subscriptions.stream()
            .map(Subscription::getStudentId)
            .distinct()
            .map(studentServicePort::getById)
            .collect(Collectors.toMap(StudentResponse::getId, Function.identity()));

        val courseResponses = subscriptions.stream().collect(Collectors.groupingBy(Subscription::getCourseId)).entrySet().stream()
            .map(entry -> mapCourse(entry, courses, students))
            .collect(Collectors.toList());

        return mapper.coursesResponsesToSubscriptionsResponse(courseResponses);
    }

    private CourseResponse mapCourse(
        final Map.Entry<CourseId, List<Subscription>> entry,
        final Map<Long, io.metadata.api.courses.CourseResponse> courses,
        final Map<Long, StudentResponse> students)
    {
        val courseResponse = courses.get(entry.getKey().getValue());
        val studentResponses = entry.getValue().stream()
            .map(a -> students.get(a.getStudentId().getValue()))
            .collect(Collectors.toList());
        return mapper.entryToCourseResponse(courseResponse, studentResponses);
    }
}
