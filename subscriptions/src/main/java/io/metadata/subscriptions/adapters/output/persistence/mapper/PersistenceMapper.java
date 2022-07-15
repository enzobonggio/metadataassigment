package io.metadata.subscriptions.adapters.output.persistence.mapper;

import io.metadata.subscriptions.adapters.output.persistence.entity.CourseEntity;
import io.metadata.subscriptions.adapters.output.persistence.entity.StudentEntity;
import io.metadata.subscriptions.adapters.output.persistence.entity.SubscriptionEntity;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.model.Subscription;
import java.sql.Timestamp;
import java.time.Instant;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersistenceMapper
{

    @Mappings({
        @Mapping(expression = "java(now())", target = "createdAt"),
        @Mapping(source = "courseId.value", target = "courseId"),
        @Mapping(source = "studentId.value", target = "studentId"),
    })
    SubscriptionEntity domainToEntity(Subscription subscription);

    @Mappings({
        @Mapping(expression = "java(now())", target = "createdAt"),
        @Mapping(source = "value", target = "id"),
    })
    StudentEntity domainToEntity(StudentId studentId);

    @Mappings({
        @Mapping(expression = "java(now())", target = "createdAt"),
        @Mapping(source = "value", target = "id"),
    })
    CourseEntity domainToEntity(CourseId courseId);

    Subscription entityToDomain(SubscriptionEntity entity);

    default StudentId entityToDomain(StudentEntity entity)
    {
        return StudentId.of(entity.getId());
    }

    default CourseId entityToDomain(CourseEntity courseEntity)
    {
        return CourseId.of(courseEntity.getId());
    }

    default Timestamp now()
    {
        return Timestamp.from(Instant.now());
    }

    default StudentId mapStudentId(Long id)
    {
        return StudentId.of(id);
    }

    default CourseId mapCourseId(Long id)
    {
        return CourseId.of(id);
    }
}
