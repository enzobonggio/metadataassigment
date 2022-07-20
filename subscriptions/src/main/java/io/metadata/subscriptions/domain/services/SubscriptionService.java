package io.metadata.subscriptions.domain.services;

import io.metadata.api.courses.CourseResponse;
import io.metadata.api.students.StudentResponse;
import io.metadata.api.subscriptions.SubscriptionResponse;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.model.Subscription;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.FetchSubscriptionsByStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscribeToCourseUseCase;
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort;
import io.metadata.subscriptions.domain.ports.output.CourseServicePort;
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort;
import io.metadata.subscriptions.domain.ports.output.StudentServicePort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort;
import io.metadata.subscriptions.domain.services.exception.CourseNotFoundException;
import io.metadata.subscriptions.domain.services.exception.StudentNotFoundException;
import io.metadata.subscriptions.domain.services.exception.SubscriptionNotAllowedException;
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper;
import io.micrometer.core.annotation.Timed;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@RequiredArgsConstructor
@Slf4j
public class SubscriptionService implements
    SubscribeToCourseUseCase,
    FetchSubscriptionsByCourseUseCase,
    FetchSubscriptionsByStudentUseCase
{
    final SubscriptionOutputPort subscriptionOutputPort;
    final CourseOutputPort courseOutputPort;
    final StudentOutputPort studentOutputPort;
    final StudentServicePort studentServicePort;
    final CourseServicePort courseServicePort;
    final Long maxCoursesPerStudent;
    final Long maxStudentsPerCourse;
    final ServiceMapper mapper;

    @Override
    @Timed
    public Collection<CourseResponse> fetchByStudentId(final Long studentId)
    {
        val id = mapper.mapStudentId(studentId);
        val subscriptions = subscriptionOutputPort.fetchByStudentId(id);

        return subscriptions.stream()
            .map(Subscription::getCourseId)
            .map(courseServicePort::getById)
            .collect(Collectors.toList());
    }

    @Override
    @Timed
    public Collection<StudentResponse> fetchByCourseId(final Long courseId)
    {
        val id = mapper.mapCourseId(courseId);
        val subscriptions = subscriptionOutputPort.fetchByCourseId(id);

        return subscriptions.stream()
            .map(Subscription::getStudentId)
            .map(studentServicePort::getById)
            .collect(Collectors.toList());
    }

    @Override
    @Timed
    public SubscriptionResponse subscribe(final Command command)
    {
        val subscription = mapper.commandToDomain(command);

        val courseId = subscription.getCourseId();
        if (!courseOutputPort.exists(courseId)) {
            throw new CourseNotFoundException();
        }

        val studentId = subscription.getStudentId();
        if (!studentOutputPort.exists(studentId)) {
            throw new StudentNotFoundException();
        }

        validateMaxCoursesPerStudent(studentId);

        validateMaxStudentsPerCourse(courseId);

        val savedSubscription = subscriptionOutputPort.save(subscription);
        return mapper.domainToResponse(savedSubscription);
    }

    private void validateMaxStudentsPerCourse(final CourseId courseId)
    {
        val studentsCount = subscriptionOutputPort.countByCourseId(courseId);

        if (studentsCount >= maxStudentsPerCourse) {
            throw new SubscriptionNotAllowedException();
        }
    }

    private void validateMaxCoursesPerStudent(final StudentId studentId)
    {
        val coursesCount = subscriptionOutputPort.countByStudentId(studentId);

        if (coursesCount >= maxCoursesPerStudent) {
            throw new SubscriptionNotAllowedException();
        }
    }
}
