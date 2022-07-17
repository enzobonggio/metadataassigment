package io.metadata.courses.adapters.input.messageconsumer.mapper;

import io.metadata.api.Subscriptions;
import io.metadata.api.courses.CourseRequest;
import io.metadata.courses.domain.ports.input.CreateCourseUseCase;
import io.metadata.courses.domain.ports.input.DeleteCourseUseCase;
import io.metadata.courses.domain.ports.input.UpdateCourseStateUseCase;
import io.metadata.courses.domain.ports.input.UpdateCourseUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MessageConsumerMapper
{
    @Mapping(target = "state", expression = "java(io.metadata.api.commons.State.CREATED)")
    UpdateCourseStateUseCase.Command messageToCommand(Subscriptions.CourseMessage message);

}
